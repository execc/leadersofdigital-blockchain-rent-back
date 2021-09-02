package com.wavesplatform.we.app.rent.service

import com.wavesplatform.vst.contract.factory.ContractClientFactory
import com.wavesplatform.we.app.rent.contract.RentContract
import org.springframework.stereotype.Service

@Service
class RentContractService(
    val factory: ContractClientFactory<RentContract>
) {
        fun create(): String {
                val api = factory.client { it.contractName("rent") }
                api.contract().create()
                return api.lastTxId
        }

        fun invoke(id: String): String {
                val api = factory.client { it.contractId(id) }
                api.contract().invoke()
                return api.lastTxId
        }
}
