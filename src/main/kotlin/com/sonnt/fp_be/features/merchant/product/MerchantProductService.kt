package com.sonnt.fp_be.features.merchant.product

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.shared.services.BaseService
import com.sonnt.fp_be.features.merchant.product.response.ProductDTO
import com.sonnt.fp_be.model.entities.extension.toProductDTO
import com.sonnt.fp_be.repos.MerchantRepo
import com.sonnt.fp_be.repos.product.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class MerchantProductService: BaseService() {
    @Autowired
    lateinit var productRepo: ProductRepo
    @Autowired
    lateinit var productCategoryRepo: ProductCategoryRepo
    @Autowired
    lateinit var productTagRepo: ProductTagRepo
    @Autowired
    lateinit var merchantRepo: MerchantRepo

    fun getProductByMerchantId(merchantId: Long?, page: Int? = 0, size: Int? = 20): List<ProductDTO> {
        val pageable = PageRequest.of(page ?: 0, size ?: 20)
        return productRepo.findProductByMerchantId(merchantId, pageable).toProductDTO()
    }

    fun addProduct(productDTO: ProductDTO) {

        checkValidFoodParams(productDTO)

        val product = productDTO.toDbModel()

        productRepo.save(product)
        productRepo.flush()
    }

    fun editProduct(productDTO: ProductDTO) {
        if(!productRepo.existsById(productDTO.id ?: 0))
            throw BusinessException(FPResponseStatus.productNotFound)

        checkValidFoodParams(productDTO)

        val product = productDTO.toDbModel()

        productRepo.save(product)
        productRepo.flush()
    }

    @Throws(Exception::class)
    fun checkValidFoodParams(productDTO: ProductDTO) {
        val merchantId = productDTO.merchantId ?: throw BusinessException(FPResponseStatus.merchantNotFound)

        if(userId != merchantId)
            throw BusinessException(FPResponseStatus.unauthorized)

        if(!merchantRepo.existsById(merchantId))
            throw BusinessException(FPResponseStatus.merchantNotFound)

        if (!productCategoryRepo.existsById(productDTO.categoryId ?: 0))
            throw BusinessException(FPResponseStatus.categoryNotFound)

        if (productDTO.tagId != null && !productTagRepo.existsById(productDTO.tagId!!))
            throw BusinessException(FPResponseStatus.tagNotFound)

    }

}