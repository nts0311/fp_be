package com.sonnt.fp_be.features.enduser.home.response

import com.sonnt.fp_be.features.enduser.home.model.HomeSection
import com.sonnt.fp_be.features.shared.dto.MerchantItemDTO
import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.model.dto.response.BaseResponse

class GetHomeInfoResponse(
    var categories: List<ProductCategoryDTO> = listOf(),
    var sections: List<HomeSection> = listOf(),
    var nearbyRestaurant: List<MerchantItemDTO> = listOf()
): BaseResponse()