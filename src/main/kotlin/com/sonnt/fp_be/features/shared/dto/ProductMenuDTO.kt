package com.sonnt.fp_be.features.shared.dto

import com.sonnt.fp_be.model.entities.Merchant
import com.sonnt.fp_be.model.entities.product.ProductMenu
import com.sonnt.fp_be.utils.sharedModelMapper

class ProductMenuDTO(
    var id: Long? = null,
    var name: String? = null,
    var merchantId: Long? = null
) {
    fun toDb(): ProductMenu = sharedModelMapper.map(this, ProductMenu::class.java).apply {
        merchantId?.also {
            this.merchant = Merchant(id = it)
        }
    }
}