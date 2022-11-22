package com.sonnt.fp_be.features.merchant.stats.dto

import com.sonnt.fp_be.model.dto.response.BaseResponse
import com.sonnt.fp_be.model.entities.product.ProductMenu

class ProductStatsResponse(
    val productStats: List<ProductCountStat>
): BaseResponse()

data class ProductCountStat(
    val productName: String,
    val imageUrl: String?,
    val count: Int
)
