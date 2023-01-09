package com.sonnt.fp_be.model.entities

import com.sonnt.fp_be.model.entities.order.OrderStatus
import com.sonnt.fp_be.model.entities.stats.DriverStat
import java.time.LocalDateTime
import javax.persistence.*

enum class DriverStatus(val value: String) {
    INACTIVE("INACTIVE"),
    IDLE("IDLE"),
    QUEUE_FOR_NEW_ORDER("QUEUE_FOR_NEW_ORDER"),
    DELIVERING("DELIVERING"),
}

@Entity
class Driver(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
    @JoinColumn(name = "account_id")
    var account: Account,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
    @JoinColumn(name = "driver_stat_id")
    val stat: DriverStat = DriverStat(),

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "last_location_id")
    var lastLocation: Address? = null,

    @Enumerated(EnumType.STRING)
    var status: DriverStatus = DriverStatus.IDLE,

    var plate: String? = null,

    var lastUpdateTime: LocalDateTime? = null

) {
}