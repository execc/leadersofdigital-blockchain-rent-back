package com.wavesplatform.we.app.rent.domain

import java.util.UUID
import javax.persistence.CascadeType.ALL
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.FetchType.EAGER
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.OneToMany
import javax.persistence.OrderColumn

@Entity
data class RentContractEntity(
    @Id
    val id: String,
    val bankAddress: String,
    val bankName: String,
    val bankUUID: UUID,
    val tenantAddress: String,
    val tenantName: String,
    val tenantUUID: UUID,
    @Embedded
    val conditions: ContractConditions,
    @Enumerated(STRING)
    val status: ContractStatus,
    val totalEarnings: Double = 0.0,
    val totalDebt: Double = 0.0,
    val creditDebt: Double = 0.0,
    val concessionDebt: Double = 0.0,
    val concessionEarnings: Double = 0.0,
    @OneToMany(fetch = EAGER, cascade = [ALL])
    @JoinTable(
            name = "CONTRACT_EVENTS",
            joinColumns = [JoinColumn(name = "contract_id")],
            inverseJoinColumns = [JoinColumn(name = "event_id")]
    )
    @OrderColumn(name = "event_order")
    val events: List<PaymentEvent>
)
