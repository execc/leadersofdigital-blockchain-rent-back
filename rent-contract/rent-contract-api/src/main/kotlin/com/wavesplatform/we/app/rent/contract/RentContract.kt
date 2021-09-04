package com.wavesplatform.we.app.rent.contract

import com.wavesplatform.vst.contract.ContractAction
import com.wavesplatform.vst.contract.ContractInit

interface RentContract {

    // TODO: Tenant name, place, date, end data, date start, earningsPercent
    @ContractInit
    fun create(
        tenant: String,
        bank: String,
        paymentAmount: Double,
        minGuaranteedConcession: Double,
        earningPercent: Double,
        concessionPercent: Double,
        place: String,
        date: String,
        endDate: String
    )

    @ContractAction
    fun enterCreditConditions(
        interestRate: Double,
        limit: Double,
        earningCreditPercent: Double
    )

    @ContractAction
    fun acceptCreditConditions()

    @ContractAction
    fun enterEarning(
        amount: Double
    )

    @ContractAction
    fun takeRent()

    @ContractAction
    fun payCredit(
        amount: Double
    )

    @ContractAction
    fun payDebt(
        amount: Double
    )
}
