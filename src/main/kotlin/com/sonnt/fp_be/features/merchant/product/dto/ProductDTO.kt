package com.sonnt.fp_be.features.merchant.product.dto

import com.sonnt.fp_be.model.entities.product.*
import com.sonnt.fp_be.utils.sharedModelMapper
import org.jetbrains.annotations.NotNull

data class ProductDTO(
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null,
    var imageUrl: String? = null,
    var price: Double? = null,
    var status: String? = null,
    var categoryId: Long? = null,
    var merchantId: Long? = null,
    var tagId: Long? = null,
    var attributes: List<ProductAttributeDTO> = listOf()
){
    fun toDbModel(): Product {
        val attributesDb = attributes.toDbAttributes()

        return sharedModelMapper.map(this, Product::class.java).also {
            it.attributes = attributesDb
            it.status = ProductStatus.valueOf(this.status ?: ProductStatus.AVAILABLE.value)
        }
    }
}

data class ProductAttributeDTO(
    var id: Long? = null,
    var isMultiple: Boolean? = null,
    var isRequired: Boolean? = null,
    var productId: Long? = null,
    var name: String? = null,
    var options: List<ProductAttributeOptionDTO> = listOf()
){
    fun toDbModel(): ProductAttribute {
        val optionsDb = options.toDbOptions()
        return sharedModelMapper.map(this, ProductAttribute::class.java).apply {
            this.options = optionsDb
        }
    }
}

fun List<ProductAttributeDTO>.toDbAttributes(): List<ProductAttribute> {
    return this.map { it.toDbModel() }
}

data class ProductAttributeOptionDTO(
    var id: Long? = null,
    var name: String? = null,
    var price: Double? = null,
    var productAttributeId: Long? = null
){
    fun toDbModel(): ProductAttributeOption {
        return sharedModelMapper.map(this, ProductAttributeOption::class.java)
    }
}

fun List<ProductAttributeOptionDTO>.toDbOptions(): List<ProductAttributeOption> {
    return this.map { it.toDbModel() }
}

