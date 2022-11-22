package com.sonnt.fp_be.features.shared.dto

import com.sonnt.fp_be.model.entities.Merchant
import com.sonnt.fp_be.model.entities.product.*
import com.sonnt.fp_be.utils.sharedModelMapper

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

        return sharedModelMapper.map(this, Product::class.java).also {product ->
            product.attributes = attributesDb.toMutableList()
            product.status = ProductStatus.valueOf(this.status ?: ProductStatus.AVAILABLE.value)

            merchantId?.also { product.merchant = Merchant(id = it) }

            categoryId?.also { product.category = ProductCategory(id = it) }

            tagId?.also{ product.tag = ProductMenu(id = it) }
        }
    }

    override fun hashCode(): Int {
        return id?.toInt() ?: 0
    }
}

data class ProductAttributeDTO(
    var id: Long? = null,
    var multiple: Boolean = false,
    var required: Boolean = false,
    var productId: Long? = null,
    var name: String? = null,
    var options: List<ProductAttributeOptionDTO> = listOf()
){
    fun toDbModel(): ProductAttribute {
        val optionsDb = options.toDbOptions()
        return sharedModelMapper.map(this, ProductAttribute::class.java).apply {
            this.options = optionsDb.toMutableList()
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

