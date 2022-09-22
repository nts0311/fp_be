package com.sonnt.fp_be.model.entities.order

import com.sonnt.fp_be.model.entities.*
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
    var items: MutableList<OrderItem> = mutableListOf(),

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
    @JoinColumn(name = "customer_id")
    var customer: Customer? = null,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
    @JoinColumn(name = "merchant_id")
    var merchant: Merchant? = null,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
    @JoinColumn(name = "driver_id")
    var driver: Driver? = null,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
    @JoinColumn(name = "receiving_address_id")
    var receivingAddress: Address? = null,
) {
}