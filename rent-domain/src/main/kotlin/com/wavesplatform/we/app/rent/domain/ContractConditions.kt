package com.wavesplatform.we.app.rent.domain

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class ContractConditions(
    val conditionsId: String,
    val paymentAmount: Double,
    val interestRate: Double = 0.0,
    @Column(name = "cond_limit")
    val limit: Double = 0.0,
    val earningPercent: Double,
    val minGuaranteedConcession: Double,
    val concessionPercent: Double,
    val earningCreditPercent: Double = 0.0,
    val date: String,
    val endDate: String
)
