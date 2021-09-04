package com.wavesplatform.we.app.rent.api

data class CreateRentalAgreementRq(
    val tenant: String,
    val bank: String,
    val paymentAmount: Double,
    val earningPercent: Double,
    val concessionPercent: Double,
    val minGuaranteedAmount: Double,
    val place: String,
    val date: String,
    val endDate: String
)
