package com.sonnt.fp_be.features.shared.response

import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.model.dto.response.BaseResponse

class ProductCategoryResponse(
    val categories: List<ProductCategoryDTO>
): BaseResponse()