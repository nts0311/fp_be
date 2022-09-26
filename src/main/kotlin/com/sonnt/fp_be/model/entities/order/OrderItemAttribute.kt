package com.sonnt.fp_be.model.entities.order

import com.sonnt.fp_be.model.entities.product.ProductAttribute
import javax.persistence.*

@Entity
class OrderItemAttribute(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "attribute_id",
        referencedColumnName = "id"
    )
    var attribute: ProductAttribute = ProductAttribute(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "order_item_attribute_id")
    var options: MutableList<OrderItemAttributeOption> = mutableListOf()
) {
}