package com.sonnt.fp_be.model.entities.order

import com.sonnt.fp_be.model.entities.product.ProductAttributeOption
import javax.persistence.*


@Entity
class OrderItemAttributeOption(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "attribute_option_id",
        referencedColumnName = "id"
    )
    var option: ProductAttributeOption = ProductAttributeOption(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "order_item_attribute_id",
        referencedColumnName = "id"
    )
    var attribute: OrderItemAttribute = OrderItemAttribute(),
) {
}