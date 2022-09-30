package com.sonnt.fp_be.model.entities.order

import com.sonnt.fp_be.model.entities.*
import com.sonnt.fp_be.model.entities.product.ProductStatus
import java.time.LocalDateTime
import javax.persistence.*

enum class OrderStatus(val value: String) {
    INIT("INIT"),
    SEARCHING_DRIVER("SEARCHING_DRIVER"),
    PREPARING("PREPARING"),
    PICKING_UP("PICKING_UP"),
    DELIVERING("DELIVERING"),
    SUCCEED("SUCCEED"),
    CANCELED("CANCELED")
}

@Entity(name = "orders")
class OrderRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    var code: String? = null,

    var createDate: LocalDateTime = LocalDateTime.now(),

    var note: String? = null,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "order_id")
    var items: MutableList<OrderItem> = mutableListOf(),

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    var customer: Customer? = null,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "merchant_id")
    var merchant: Merchant? = null,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "driver_id")
    var driver: Driver? = null,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "receiving_address_id")
    var receivingAddress: Address? = null,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
    @JoinColumn(name = "estimated_route_info_id")
    var estimatedRouteInfo: OrderEstimatedRouteInfo? = null,

    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.INIT
)