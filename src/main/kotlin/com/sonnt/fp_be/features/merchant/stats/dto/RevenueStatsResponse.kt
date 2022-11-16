package com.sonnt.fp_be.features.merchant.stats.dto

import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.features.shared.dto.ProductDTO
import com.sonnt.fp_be.model.dto.response.BaseResponse
import com.sonnt.fp_be.model.entities.product.ProductCategory
import com.sonnt.fp_be.model.entities.product.ProductMenu

class RevenueStatsResponse(
    val revenueByDay: List<DayRevenueStat>,
    val revenueByCategory: List<CategoriesRevenueStat>,
    val revenueByMenu: List<MenuRevenueStat>,
    val mostSoldProduct: ProductRevenueStat?,
    val leastSoldProduct: ProductRevenueStat?
): BaseResponse()

data class DayRevenueStat(
    val date: String,
    val revenue: Double
)

data class CategoriesRevenueStat(
    val category: ProductCategoryDTO,
    val percentage: Double
)

data class MenuRevenueStat(
    val menu: ProductMenu,
    val percentage: Double
)

data class ProductRevenueStat(
    val product: ProductDTO,
    val revenue: Double,
    val count: Int
)