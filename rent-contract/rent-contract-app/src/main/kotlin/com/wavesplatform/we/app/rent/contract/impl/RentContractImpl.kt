package com.wavesplatform.we.app.rent.contract.impl

import com.wavesplatform.vst.contract.data.ContractCall
import com.wavesplatform.vst.contract.mapping.Mapping
import com.wavesplatform.vst.contract.spring.annotation.ContractHandlerBean
import com.wavesplatform.vst.contract.state.ContractState
import com.wavesplatform.vst.contract.state.getValue
import com.wavesplatform.vst.contract.state.set
import com.wavesplatform.vst.contract.state.setValue
import com.wavesplatform.we.app.rent.contract.RentContract
import com.wavesplatform.we.app.rent.domain.ContractConditions
import com.wavesplatform.we.app.rent.domain.ContractStatus
import com.wavesplatform.we.app.rent.domain.ContractStatus.ACCEPTED
import com.wavesplatform.we.app.rent.domain.ContractStatus.BANK_PROPOSED
import com.wavesplatform.we.app.rent.domain.ContractStatus.NEW
import com.wavesplatform.we.app.rent.domain.PaymentEvent
import java.util.Date

@ContractHandlerBean
class RentContractImpl(
    state: ContractState,
    private val call: ContractCall
) : RentContract {

    // Contract parties
    //
    private var landloardAddress: String? by state
    private var tenantAddress: String? by state
    private var bankAddress: String? by state

    private var place: String? by state

    // Contract conditions
    //
    private val conditions: Mapping<ContractConditions> by state
    private var currentConditions: String? by state

    // Transient variables
    //
    private var status: ContractStatus? by state
    private var totalEarnings: Double? by state
    private var totalDebt: Double? by state
    private var creditDebt: Double? by state

    // Contract events
    //
    private val events: Mapping<PaymentEvent> by state

    override fun create(tenant: String, bank: String, paymentAmount: Double, earningPercent: Double, place: String, date: String, endDate: String) {
        require(tenant.isNotBlank()) { "TENANT_ADDRESS_NOT_SET" }
        require(bank.isNotBlank()) { "BANK_ADDRESS_NOT_SET" }
        require(paymentAmount > 0) { "INVALID_PAYMENT_AMOUNT" }
        require(earningPercent > 0 && earningPercent < 100) { "INVALID_EARNING_PERCENT_RATE" }

        landloardAddress = call.sender
        bankAddress = call.sender
        tenantAddress = tenant
        status = NEW

        val conditions = ContractConditions(
                conditionsId = call.id,
                paymentAmount = paymentAmount,
                earningPercent = earningPercent,
                date = date,
                endDate = endDate
        )

        currentConditions = conditions.conditionsId

        this.conditions[conditions.conditionsId] = conditions

        this.place = place
        this.totalDebt = 0.0
        this.creditDebt = 0.0
        this.totalEarnings = 0.0

        this.totalDebt = conditions().paymentAmount
    }

    override fun enterCreditConditions(interestRate: Double, limit: Double, earningCreditPercent: Double) {
        require(call.sender == bankAddress) { "INVALID_SENDER" }
        require(interestRate > 0 && interestRate < 100) { "INVALID_INTEREST_RATE" }
        require(earningCreditPercent > 0 && earningCreditPercent < 100) { "INVALID_EARNING_PERCENT_RATE" }
        require(limit > 0) { "INVALID_LIMIT" }

        status = BANK_PROPOSED
        this.conditions[this.currentConditions!!] = this.conditions[this.currentConditions!!].copy(
                interestRate = interestRate,
                limit = limit,
                earningCreditPercent = earningCreditPercent
        )
    }

    override fun acceptCreditConditions() {
        require(call.sender == tenantAddress) { "INVALID_SENDER" }
        status = ACCEPTED
    }

    override fun enterEarning(amount: Double) {
        require(call.sender == tenantAddress) { "INVALID_SENDER" }
        require(amount > 0) { "INVALID_LIMIT" }

        var debtPart = 0.0
        var creditPart = 0.0

        if (totalDebt!! > 0) {
            debtPart = amount * conditions().earningPercent / 100.0
            if (debtPart > totalDebt!!) {
                debtPart = totalDebt!!
            }
        }

        if (creditDebt!! > 0) {
            creditPart = amount * conditions().earningCreditPercent / 100.0
            if (debtPart > creditDebt!!) {
                debtPart = creditDebt!!
            }
        }

        val earnings = amount - debtPart - creditPart

        this.totalEarnings = this.totalEarnings!! + earnings
        if (debtPart > 0) {
            this.totalDebt = this.totalDebt!! - debtPart
        }
        if (creditPart > 0) {
            this.creditDebt = this.creditDebt!! - creditPart
        }

        events[call.id] = PaymentEvent(
                id = call.id,
                type = "PAYMENT",
                amount = amount,
                debtPart = debtPart,
                creditPart = creditPart,
                earning = earnings,
                date = Date()
        )
    }

    override fun takeRent() {
        require(call.sender == landloardAddress) { "INVALID_SENDER" }

        if (totalDebt!! > 0) {
            creditDebt = creditDebt!! + totalDebt!!
        }
        totalDebt = conditions().paymentAmount

        events[call.id] = PaymentEvent(
                id = call.id,
                type = "RENT",
                amount = conditions().paymentAmount,
                debtPart = 0.0,
                creditPart = creditDebt!!,
                earning = 0.0,
                date = Date()
        )
    }

    override fun payCredit(amount: Double) {
        require(call.sender == bankAddress) { "INVALID_SENDER" }
        require(amount > 0 && amount <= creditDebt!!) { "INVALID_AMOUNT" }

        creditDebt = creditDebt!! - amount

        events[call.id] = PaymentEvent(
                id = call.id,
                type = "PAY_CREDIT",
                amount = amount,
                debtPart = 0.0,
                creditPart = 0.0,
                earning = 0.0,
                date = Date()
        )
    }

    override fun payDebt(amount: Double) {
        require(call.sender == bankAddress) { "INVALID_SENDER" }
        require(amount > 0 && amount <= totalDebt!!) { "INVALID_AMOUNT" }

        totalDebt = totalDebt!! - amount

        events[call.id] = PaymentEvent(
                id = call.id,
                type = "PAY_DEBT",
                amount = amount,
                debtPart = 0.0,
                creditPart = 0.0,
                earning = 0.0,
                date = Date()
        )
    }

    fun conditions() = this.conditions[this.currentConditions!!]!!
}
