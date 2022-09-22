package com.sonnt.fp_be.features.enduser.order.model


data class OrderEstimatedRouteInfo(
    val durationInSec: Long,
    val distanceInMeter: Long,
    val distanceReadable: String
)

data class OrderPaymentInfo(
    val price: Double,
    val deliveryFee: Double,
    val serviceFee: Double,
    val discount: Double
)

data class OrderCheckinInfo(
    val estimatedRouteInfo: OrderEstimatedRouteInfo,
    val paymentInfo: OrderPaymentInfo
)