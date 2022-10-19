package com.sonnt.fp_be.features.enduser.order.response

import com.sonnt.fp_be.model.dto.response.BaseResponse

data class GetActiveOrderResponse(
    val orderInfo: OrderInfo?
): BaseResponse()