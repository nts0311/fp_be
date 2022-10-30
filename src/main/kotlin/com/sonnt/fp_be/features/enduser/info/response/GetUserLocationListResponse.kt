package com.sonnt.fp_be.features.enduser.info.response

import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import com.sonnt.fp_be.features.shared.dto.MerchantItemDTO
import com.sonnt.fp_be.model.dto.response.BaseResponse

class GetUserLocationListResponse(
    val locations: List<FPAddressDTO>
): BaseResponse()