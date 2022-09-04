package com.sonnt.fp_be.features.enduser.home.model

import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.features.shared.dto.MerchantItemDTO

data class HomeSection(
    val name: String? = null,
    val description: String? = null,
    val bannerImg: String? = null,
    var listMerchant: List<MerchantItemDTO> = listOf()
)

data class HomeInfo(
    var categories: List<ProductCategoryDTO>,
    var sections: List<HomeSection>,
    var nearbyRestaurant: List<MerchantItemDTO>
)
