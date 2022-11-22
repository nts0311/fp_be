package com.sonnt.fp_be.features.merchant.stats.dto

import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.features.shared.dto.ProductDTO
import com.sonnt.fp_be.features.shared.dto.ProductMenuDTO
import com.sonnt.fp_be.model.dto.response.BaseResponse
import com.sonnt.fp_be.model.entities.product.ProductCategory
import com.sonnt.fp_be.model.entities.product.ProductMenu

class RevenueStatsResponse(
    val revenueByDay: List<DayRevenueStat>,
    val revenueByCategory: List<CategoriesRevenueStat>,
    val revenueByMenu: List<MenuRevenueStat>,
    val revenueByProduct: List<ProductRevenueStat>
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
    val menu: ProductMenuDTO,
    val percentage: Double
)

data class ProductRevenueStat(
    val productName: String,
    val productImageUrl: String,
    val productCategoryName: String,
    val productMenuName: String,
    val productPrice: Double,
    val revenue: Double,
    val count: Int
)