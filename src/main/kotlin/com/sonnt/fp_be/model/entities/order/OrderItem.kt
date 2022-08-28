package com.sonnt.fp_be.model.entities.order

import com.sonnt.fp_be.model.entities.product.Product
import javax.persistence.*

@Entity
class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "order_id",
        referencedColumnName = "id"
    )
    var orderItem: OrderRecord = OrderRecord(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_id",
        referencedColumnName = "id"
    )
    var product: Product = Product(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderItem")
    var attributes: List<OrderItemAttribute> = listOf()
) {
}