package com.sonnt.fp_be.features.enduser.order.response

import com.sonnt.fp_be.features.enduser.order.model.OrderEstimatedRouteInfo
import com.sonnt.fp_be.features.enduser.order.model.OrderPaymentInfo
import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import java.time.LocalDateTime

data class OrderedProductItem(
    val name: String,
    val num: Int
)

data class GetOrderInfoResponse(
    val orderId: Long,
    val orderStatus: String,
    val createdDate: LocalDateTime,
    val fromAddress: FPAddressDTO,
    val toAddress: FPAddressDTO,
    val routeInfo: OrderEstimatedRouteInfo,
    val item: List<OrderedProductItem>,
    val paymentInfo: OrderPaymentInfo
)