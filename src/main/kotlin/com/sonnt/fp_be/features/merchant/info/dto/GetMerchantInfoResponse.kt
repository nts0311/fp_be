package com.sonnt.fp_be.features.merchant.info.dto

import com.sonnt.fp_be.model.dto.response.BaseResponse

class GetMerchantInfoResponse(
    val isOpening: Boolean,
    val openingHour: String,
    val closingHour: String
): BaseResponse()