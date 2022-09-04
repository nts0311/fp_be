package com.sonnt.fp_be.features.shared.dto

import com.sonnt.fp_be.model.entities.Address

data class MerchantItemDTO(
    val id: Long? = null,
    val name: String? = null,
    val subTitle: String? = null,
    val imageUrl: String? = null,
    var distance: Double? = null,
    val numStar: Double? = null,
    val numOrder: Long? = null,
    val address: Address? = null
)