package com.sonnt.fp_be.features.merchant.tag

import com.sonnt.fp_be.features.shared.dto.ProductMenuDTO
import com.sonnt.fp_be.model.dto.response.BaseResponse

class ProductTagResponse(
    val menu: ProductMenuDTO
): BaseResponse()


class ProductTagListResponse(
    val page: Int? = null,
    val size: Int? = null,
    val menus: List<ProductMenuDTO>
): BaseResponse()