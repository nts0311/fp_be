package com.sonnt.fp_be.model.entities.extension

import com.sonnt.fp_be.features.enduser.order.model.AttributeSelection
import com.sonnt.fp_be.features.enduser.order.model.UserProductSelection
import com.sonnt.fp_be.features.enduser.order.response.OrderedProductItem
import com.sonnt.fp_be.features.shared.dto.OrderDTO
import com.sonnt.fp_be.features.shared.dto.OrderItemAttributeDTO
import com.sonnt.fp_be.features.shared.dto.OrderItemDTO
import com.sonnt.fp_be.model.entities.order.OrderItem
import com.sonnt.fp_be.model.entities.order.OrderItemAttribute
import com.sonnt.fp_be.model.entities.order.OrderRecord
import com.sonnt.fp_be.repos.OrderRecordRepo
import com.sonnt.fp_be.utils.sharedModelMapper

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

fun OrderRecord.toDTO(): OrderDTO {
    return sharedModelMapper.map(this, OrderDTO::class.java).also { dto ->
        dto.status = this.status.value
        dto.orderItem = this.items.map { it.toDTO() }
    }
}

fun OrderItem.toDTO(): OrderItemDTO {
    return OrderItemDTO(
        product = this.product.toDTO().also { it.attributes = listOf() },
        num = this.num,
        attributes = this.attributes.map { it.toDTO() }
    )
}

fun OrderItemAttribute.toDTO(): OrderItemAttributeDTO {
    return OrderItemAttributeDTO(
        attribute = this.attribute.toDTO().also { it.options = listOf() },
        options = this.options.map { it.option.toDTO() }
    )
}

fun OrderItem.toOrderedProductItem(): OrderedProductItem {
    val listAttrs = this.attributes.map { orderItemAttr ->
        var result = orderItemAttr.attribute.name + ": "
        val optionsStr = orderItemAttr.options.fold("") {acc, orderItemAttributeOption ->
            "$acc, ${orderItemAttributeOption.option.name}"
        }.drop(2)

        return@map result + optionsStr
    }

    return OrderedProductItem(this.product.name, this.num, listAttrs)
}

fun OrderRecord.getCustomerUsername() = customer?.account?.username
fun OrderRecord.getDriverUsername() = driver?.account?.username
fun OrderRecord.getMerchantUsername() = merchant?.account?.username