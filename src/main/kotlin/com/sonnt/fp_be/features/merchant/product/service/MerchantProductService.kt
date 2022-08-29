package com.sonnt.fp_be.features.merchant.product.service

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.base.services.BaseService
import com.sonnt.fp_be.features.merchant.product.dto.ProductDTO
import com.sonnt.fp_be.features.shared.services.ProductCategoryService
import com.sonnt.fp_be.repos.MerchantRepo
import com.sonnt.fp_be.repos.product.*
import com.sonnt.fp_be.utils.badRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MerchantProductService: BaseService() {
    @Autowired
    lateinit var productRepo: ProductRepo
    @Autowired
    lateinit var productAttributeRepo: ProductAttributeRepo
    @Autowired
    lateinit var productAttributeOptionRepo: ProductAttributeOptionRepo
    @Autowired
    lateinit var productCategoryRepo: ProductCategoryRepo
    @Autowired
    lateinit var productTagRepo: ProductTagRepo
    @Autowired
    lateinit var merchantRepo: MerchantRepo

    fun addProduct(productDTO: ProductDTO) {
        if (!productCategoryRepo.existsById(productDTO.categoryId ?: 0))
            throw BusinessException(FPResponseStatus.categoryNotFound)

        if (productDTO.tagId != null && !productTagRepo.existsById(productDTO.tagId!!))
            throw BusinessException(FPResponseStatus.tagNotFound)

        val merchantId = productDTO.merchantId ?: throw BusinessException(FPResponseStatus.merchantNotFound)

        if(!merchantRepo.existsById(merchantId)) throw BusinessException(FPResponseStatus.merchantNotFound)

        val product = productDTO.toDbModel()

        productRepo.save(product)
        productRepo.flush()
    }
}