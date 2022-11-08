package com.sonnt.fp_be.features.enduser.home.response

import com.sonnt.fp_be.features.shared.dto.MerchantItemDTO
import com.sonnt.fp_be.model.dto.response.BaseResponse

class FindMerchantResponse(
    val merchants: List<MerchantItemDTO>
): BaseResponse()