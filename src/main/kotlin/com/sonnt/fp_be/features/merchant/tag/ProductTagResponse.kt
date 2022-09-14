package com.sonnt.fp_be.features.merchant.tag

import com.sonnt.fp_be.features.shared.dto.ProductTagDTO
import com.sonnt.fp_be.model.dto.response.BaseResponse

class ProductTagResponse(
    val tag: ProductTagDTO
): BaseResponse()


class ProductTagListResponse(
    val page: Int? = null,
    val size: Int? = null,
    val menus: List<ProductTagDTO>
): BaseResponse()