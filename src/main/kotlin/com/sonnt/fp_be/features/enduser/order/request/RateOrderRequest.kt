package com.sonnt.fp_be.features.enduser.order.request

data class RateOrderRequest(
    val merchantStar: Int,
    val driverStat: Int,
    val orderId: Long
)