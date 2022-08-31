package com.sonnt.fp_be.features.shared.dto

import com.sonnt.fp_be.model.entities.Merchant
import com.sonnt.fp_be.model.entities.product.ProductTag
import com.sonnt.fp_be.utils.sharedModelMapper

class ProductTagDTO(
    var id: Long? = null,
    var name: String? = null,
    var merchantId: Long? = null
) {
    fun toDb(): ProductTag = sharedModelMapper.map(this, ProductTag::class.java).apply {
        merchantId?.also {
            this.merchant = Merchant(id = it)
        }
    }
}