package com.sonnt.fp_be.features.enduser.order.response

import com.sonnt.fp_be.features.enduser.order.model.OrderCheckinInfo
import com.sonnt.fp_be.model.dto.response.BaseResponse

class GetOrderCheckinInfoResponse(
    val checkinInfo: OrderCheckinInfo
): BaseResponse()