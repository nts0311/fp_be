package com.sonnt.fp_be.model.entities.extension

import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.model.entities.product.ProductCategory
import com.sonnt.fp_be.utils.sharedModelMapper

fun ProductCategory.toDTO(): ProductCategoryDTO
    = sharedModelMapper.map(this, ProductCategoryDTO::class.java)