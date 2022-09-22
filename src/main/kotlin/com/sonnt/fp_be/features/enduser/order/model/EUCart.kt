package com.sonnt.fp_be.features.enduser.order.model


data class AttributeSelection(
    val attributeId: Long,
    val optionsId: List<Long>
)

data class CartProductItem(
    val productId: Long,
    val attributes: List<AttributeSelection>,
    val num: Int
)