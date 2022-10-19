package com.sonnt.fp_be.features.driver.order.dto

data class ConfirmReceivedOrderRequest(
    var orderId: Long = 0,
    var billImageUrl: String = ""
)
