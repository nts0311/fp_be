package com.sonnt.fp_be.features.sysadmin

import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.features.shared.services.BaseService
import com.sonnt.fp_be.model.entities.product.ProductCategory
import com.sonnt.fp_be.repos.product.ProductCategoryRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SysAdminService: BaseService() {
    @Autowired lateinit var categoryRepo: ProductCategoryRepo

    fun addCategory(categoryDTO: ProductCategoryDTO) {
        val categoryEntity = modelMapper.map(categoryDTO, ProductCategory::class.java)
        categoryRepo.save(categoryEntity)
    }
}