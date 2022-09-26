package com.sonnt.fp_be.features.enduser.order.model


data class AttributeSelection(
    val attributeId: Long,
    val optionsId: List<Long>
)

data class UserProductSelection(
    val productId: Long,
    val attributeSelections: List<AttributeSelection>,
    val num: Int
)