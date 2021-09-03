package com.wavesplatform.we.app.rent.domain

import java.util.Date
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class PaymentEvent(
    @Id
    val id: String,
    val type: String,
    val amount: Double,
    val earning: Double,
    val debtPart: Double,
    val creditPart: Double,
    val date: Date
)
