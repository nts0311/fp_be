package com.sonnt.fp_be.model.entities.order

import com.sonnt.fp_be.model.entities.RouteInfo
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "orders")
class OrderRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    var code: String = "",

    var createDate: LocalDateTime = LocalDateTime.now(),

    var note: String = "",

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderItem")
    var items: List<OrderItem> = listOf(),

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
    @JoinColumn(name = "route_info_id")
    val routeInfo: RouteInfo = RouteInfo()
) {
}