package com.sonnt.fp_be.features.shared.services

import com.sonnt.fp_be.repos.product.ProductCategoryRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductCategoryService: BaseService() {
    @Autowired
    lateinit var productCategoryRepo: ProductCategoryRepo

    fun isCategoryExist(id: Long) = productCategoryRepo.existsById(id)
}