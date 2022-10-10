package com.sonnt.fp_be.features.enduser.order.response

import com.sonnt.fp_be.features.enduser.order.model.OrderPaymentInfo
import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import com.sonnt.fp_be.model.entities.order.OrderEstimatedRouteInfo
import java.time.LocalDateTime

data class OrderedProductItem(
    val name: String,
    val num: Int,
    val attributes: List<String>
)

data class OrderInfo(
    val orderId: Long,
    val orderStatus: String,
    val createdDate: LocalDateTime,
    val fromAddress: FPAddressDTO,
    val toAddress: FPAddressDTO,
    val routeInfo: OrderEstimatedRouteInfo,
    val item: List<OrderedProductItem>,
    val paymentInfo: OrderPaymentInfo,
    val customerName: String,
    val customerPhone: String,
    val merchantName: String,
    val merchantPhone: String,
    val driverName: String?,
    val driverPhone: String?,
    val driverPlate: String?
)