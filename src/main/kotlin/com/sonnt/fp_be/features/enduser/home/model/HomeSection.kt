package com.sonnt.fp_be.features.enduser.home.model

import com.sonnt.fp_be.features.shared.dto.MerchantItemDTO
import kotlin.collections.List

data class HomeSection(
    val name: String? = null,
    val description: String? = null,
    val bannerImg: String? = null,
    var listMerchant: List<MerchantItemDTO> = listOf()
)