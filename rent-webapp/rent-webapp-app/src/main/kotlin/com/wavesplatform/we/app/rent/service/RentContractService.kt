package com.wavesplatform.we.app.rent.service

import com.wavesplatform.vst.api.identity.VstCompanyApi
import com.wavesplatform.vst.contract.factory.ContractClientFactory
import com.wavesplatform.vst.node.dto.tx.CallContractTx
import com.wavesplatform.vst.tx.observer.annotation.VstBlockListener
import com.wavesplatform.vst.tx.observer.annotation.VstKeyFilter
import com.wavesplatform.vst.tx.observer.api.model.VstKeyEvent
import com.wavesplatform.we.app.rent.api.CreateRentalAgreementRq
import com.wavesplatform.we.app.rent.contract.RentContract
import com.wavesplatform.we.app.rent.db.ContractRepository
import com.wavesplatform.we.app.rent.domain.ContractConditions
import com.wavesplatform.we.app.rent.domain.ContractStatus
import com.wavesplatform.we.app.rent.domain.ContractStatus.NEW
import com.wavesplatform.we.app.rent.domain.PaymentEvent
import com.wavesplatform.we.app.rent.domain.RentContractEntity
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RentContractService(
    val factory: ContractClientFactory<RentContract>,
    val vstCompanyApi: VstCompanyApi,
    val contractRepository: ContractRepository
) {

        fun create(rq: CreateRentalAgreementRq): String {
                val bank = vstCompanyApi.getOne(UUID.fromString(rq.bank)).body!!
                val bankAddress = bank.nodes[0].ownerAddress
                val tenant = vstCompanyApi.getOne(UUID.fromString(rq.tenant)).body!!
                val tenantAddress = tenant.nodes[0].ownerAddress

                val api = factory.client { it.contractName("rent") }
                api.contract().create(
                        bank = bankAddress,
                        tenant = tenantAddress,
                        date = rq.date,
                        endDate = rq.endDate,
                        earningPercent = rq.earningPercent,
                        paymentAmount = rq.paymentAmount,
                        place = rq.place
                )

                val id = api.lastTxId

                contractRepository.save(RentContractEntity(
                        id = id,
                        bankAddress = bankAddress,
                        bankName = bank.name,
                        bankUUID = bank.id,
                        tenantAddress = tenantAddress,
                        tenantName = tenant.name,
                        tenantUUID = tenant.id,
                        events = listOf(),
                        conditions = ContractConditions(
                                conditionsId = id,
                                date = rq.date,
                                earningPercent = rq.earningPercent,
                                endDate = rq.endDate,
                                paymentAmount = rq.paymentAmount
                        ),
                        status = NEW
                ))

                return id
        }

        fun enterEarning(id: String, amount: Double): String {
                val api = factory.client { it.contractId(id) }
                api.contract().enterEarning(amount)
                return api.lastTxId
        }

    @VstBlockListener
    fun replicateEvent(@VstKeyFilter(keyPrefix = "EVENTS_") event: VstKeyEvent<PaymentEvent>) {
            if (contractRepository.existsById((event.tx.tx as CallContractTx).contractId)) {
                    val contract = contractRepository.getOne((event.tx.tx as CallContractTx).contractId)
                    contractRepository.save(contract.copy(
                            events = contract.events + event.payload
                    ))
            }
    }

    @VstBlockListener
    fun replicateTotalDebt(@VstKeyFilter(keyRegexp = "TOTAL_DEBT") event: VstKeyEvent<Double>) {
        if (event.tx.tx is CallContractTx) {
                val contractId = (event.tx.tx as CallContractTx).contractId
                if (contractRepository.existsById(contractId)) {
                        val contract = contractRepository.getOne(contractId)
                        contractRepository.save(contract.copy(
                                totalDebt = event.payload
                        ))
                }
        }
    }

        @VstBlockListener
        fun replicateCreditDebt(@VstKeyFilter(keyRegexp = "CREDIT_DEBT") event: VstKeyEvent<Double>) {
                if (event.tx.tx is CallContractTx) {
                        val contractId = (event.tx.tx as CallContractTx).contractId
                        if (contractRepository.existsById(contractId)) {
                                val contract = contractRepository.getOne(contractId)
                                contractRepository.save(contract.copy(
                                        creditDebt = event.payload
                                ))
                        }
                }
        }

        @VstBlockListener
        fun replicateTotalEarnings(@VstKeyFilter(keyRegexp = "TOTAL_EARNINGS") event: VstKeyEvent<Double>) {
                if (event.tx.tx is CallContractTx) {
                        val contractId = (event.tx.tx as CallContractTx).contractId
                        if (contractRepository.existsById(contractId)) {
                                val contract = contractRepository.getOne(contractId)
                                contractRepository.save(contract.copy(
                                        totalEarnings = event.payload
                                ))
                        }
                }
        }

        @VstBlockListener
        fun replicateStatus(@VstKeyFilter(keyRegexp = "STATUS") event: VstKeyEvent<ContractStatus>) {
                if (event.tx.tx is CallContractTx) {
                        val contractId = (event.tx.tx as CallContractTx).contractId
                        if (contractRepository.existsById(contractId)) {
                                val contract = contractRepository.getOne(contractId)
                                contractRepository.save(contract.copy(
                                        status = event.payload
                                ))
                        }
                }
        }

    fun listAll(): List<RentContractEntity> {
        return contractRepository.findAll()
    }
}
