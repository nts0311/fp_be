package com.sonnt.fp_be.model.entities.extension

import com.sonnt.fp_be.features.enduser.order.model.AttributeSelection
import com.sonnt.fp_be.features.shared.dto.ProductAttributeDTO
import com.sonnt.fp_be.features.shared.dto.ProductAttributeOptionDTO
import com.sonnt.fp_be.features.shared.dto.ProductDTO
import com.sonnt.fp_be.features.shared.dto.ProductMenuDTO
import com.sonnt.fp_be.model.entities.product.Product
import com.sonnt.fp_be.model.entities.product.ProductAttribute
import com.sonnt.fp_be.model.entities.product.ProductAttributeOption
import com.sonnt.fp_be.model.entities.product.ProductMenu
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

fun List<ProductAttribute>.toAttributeDTO() = this.map { it.toDTO() }

fun Product.toDTO(): ProductDTO {
    return sharedModelMapper.map(this, ProductDTO::class.java).also { productDTO ->
        productDTO.status = this.status.value
        productDTO.merchantId = this.merchant.id
        productDTO.categoryId = this.category.id
        productDTO.tagId = this.tag?.id
        productDTO.attributes = this.attributes.toAttributeDTO()
    }
}

fun List<Product>.toProductDTO() = this.map { it.toDTO() }

fun ProductMenu.toDTO(): ProductMenuDTO {
    return sharedModelMapper.map(this, ProductMenuDTO::class.java).apply {
        this.merchantId = merchant.id
    }
}

fun List<ProductMenu>.toProductTagDTO() = this.map { it.toDTO() }

fun Product.calculatePrice(selectedAttrs: List<AttributeSelection>): Double {
    var result = price

    for (selectedAttr in selectedAttrs) {
        val att = attributes.first { it.id == selectedAttr.attributeId }
        val optionsPrice = att.options
            .filter { selectedAttr.optionsId.contains(it.id) }
            .fold(0.0) {acc, option -> acc + option.price}

        result += optionsPrice
    }

    return result
}