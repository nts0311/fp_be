package com.sonnt.fp_be.features.shared.services

import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.model.entities.extension.toDTO
import com.sonnt.fp_be.repos.product.ProductCategoryRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductCategoryService: BaseService() {
    @Autowired
    lateinit var productCategoryRepo: ProductCategoryRepo

    fun getCategories(): List<ProductCategoryDTO> {
        return productCategoryRepo.findAll().map { it.toDTO() }
    }
}