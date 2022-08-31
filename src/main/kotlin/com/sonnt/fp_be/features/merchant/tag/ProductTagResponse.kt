package com.sonnt.fp_be.features.merchant.product.response

import com.sonnt.fp_be.features.shared.dto.ProductTagDTO
import com.sonnt.fp_be.model.dto.response.BaseResponse

class ProductTagResponse(
    val tag: ProductTagDTO
): BaseResponse()


class ProductTagListResponse(
    val page: Int? = null,
    val size: Int? = null,
    val tag: List<ProductTagDTO>
): BaseResponse()