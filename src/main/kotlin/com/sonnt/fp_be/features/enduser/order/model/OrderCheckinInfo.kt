package com.sonnt.fp_be.features.enduser.order.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.sonnt.fp_be.model.entities.order.OrderEstimatedRouteInfo


//data class OrderEstimatedRouteInfo(
//    val durationInSec: Long,
//    val distanceInMeter: Long,
//    val distanceReadable: String
//)

data class OrderPaymentInfo(
    val price: Double,
    val deliveryFee: Double,
    val serviceFee: Double,
    val discount: Double,
    @JsonIgnore val priceByProductId: Map<Long, Double>
)

data class OrderCheckinInfo(
    val estimatedRouteInfo: OrderEstimatedRouteInfo,
    val paymentInfo: OrderPaymentInfo
)