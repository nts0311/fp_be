package com.sonnt.fp_be.features.merchant.order.dto

import com.sonnt.fp_be.features.enduser.order.model.OrderCheckinInfo
import com.sonnt.fp_be.features.enduser.order.response.OrderInfo
import com.sonnt.fp_be.model.dto.response.BaseResponse

class GetMerchantActiveOrdersResponse(
    val activeOrders: List<OrderInfo>
): BaseResponse()