package com.sonnt.fp_be.model.entities.extension

import com.sonnt.fp_be.features.merchant.product.response.ProductAttributeDTO
import com.sonnt.fp_be.features.merchant.product.response.ProductAttributeOptionDTO
import com.sonnt.fp_be.features.merchant.product.response.ProductDTO
import com.sonnt.fp_be.model.entities.product.Product
import com.sonnt.fp_be.model.entities.product.ProductAttribute
import com.sonnt.fp_be.model.entities.product.ProductAttributeOption
import com.sonnt.fp_be.utils.sharedModelMapper

fun ProductAttributeOption.toDTO(): ProductAttributeOptionDTO {
    return sharedModelMapper.map(this, ProductAttributeOptionDTO::class.java)
}

fun List<ProductAttributeOption>.toAttributeOptionDTO(): List<ProductAttributeOptionDTO> {
    return this.map { it.toDTO() }
}

fun ProductAttribute.toDTO(): ProductAttributeDTO {
    val optionsDTO = options.toAttributeOptionDTO().onEach { it.productAttributeId = id }
    return sharedModelMapper.map(this, ProductAttributeDTO::class.java).apply {
        this.options = optionsDTO.toMutableList()
    }
}

fun List<ProductAttribute>.toAttributeDTO(): List<ProductAttributeDTO> {
    return this.map { it.toDTO() }
}

fun Product.toDTO(): ProductDTO {
    return sharedModelMapper.map(this, ProductDTO::class.java).also { productDTO ->
        productDTO.status = this.status.value
        productDTO.merchantId = this.merchant.id
        productDTO.categoryId = this.category.id
        productDTO.tagId = this.tag?.id
        productDTO.attributes = this.attributes.toAttributeDTO()
    }
}

fun List<Product>.toProductDTO(): List<ProductDTO> {
    return this.map { it.toDTO() }
}
