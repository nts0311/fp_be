package com.sonnt.fp_be.features.merchant.product.response

import com.sonnt.fp_be.model.dto.response.BaseResponse

class ProductListResponse(
    val page: Int? = null,
    val size: Int? = null,
    val products: List<ProductDTO>
): BaseResponse() {
}

class ProductResponse(
    val product: ProductDTO
): BaseResponse() {
}