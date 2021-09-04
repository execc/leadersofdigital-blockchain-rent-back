package com.wavesplatform.we.app.rent.api

data class CreditAgreementRq(
        val interestRate: Double,
        val limit: Double,
        val earningCreditPercent: Double
)