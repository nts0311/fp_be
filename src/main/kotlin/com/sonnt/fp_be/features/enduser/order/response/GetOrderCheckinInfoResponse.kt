package com.sonnt.fp_be.features.enduser.order.response

import com.sonnt.fp_be.features.enduser.order.model.OrderCheckinInfo
import com.sonnt.fp_be.features.enduser.order.model.OrderPaymentInfo
import com.sonnt.fp_be.model.dto.response.BaseResponse
import com.sonnt.fp_be.model.entities.order.OrderEstimatedRouteInfo

class GetOrderCheckinInfoResponse(
    val estimatedRouteInfo: OrderEstimatedRouteInfo,
    val paymentInfo: OrderPaymentInfo
): BaseResponse()