package com.sonnt.fp_be.model.entities.extension

import com.sonnt.fp_be.features.enduser.order.model.AttributeSelection
import com.sonnt.fp_be.features.enduser.order.model.UserProductSelection
import com.sonnt.fp_be.model.entities.order.OrderItemAttribute
import com.sonnt.fp_be.model.entities.order.OrderRecord

fun OrderRecord.toUserProductSelection(): List<UserProductSelection> {

    return items.map {
        UserProductSelection(
            it.product.id,
            it.attributes.map {attr -> attr.toAttrsSelection() },
            it.num
        )
    }
}

fun OrderItemAttribute.toAttrsSelection(): AttributeSelection {
    return AttributeSelection(
        attribute.id,
        options.map { it.option.id }
    )
}