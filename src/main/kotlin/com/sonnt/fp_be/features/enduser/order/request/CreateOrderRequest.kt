package com.sonnt.fp_be.features.enduser.order.request

import com.sonnt.fp_be.features.enduser.order.model.UserProductSelection

data class CreateOrderRequest(
    val addressId: Long,
    val userProductSelection: List<UserProductSelection>,
    val note: String?
)