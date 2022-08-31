package com.sonnt.fp_be.features.merchant.product

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.merchant.BaseMerchantService
import com.sonnt.fp_be.features.shared.services.BaseService
import com.sonnt.fp_be.features.merchant.product.response.ProductDTO
import com.sonnt.fp_be.model.entities.extension.toDTO
import com.sonnt.fp_be.model.entities.extension.toProductDTO
import com.sonnt.fp_be.repos.MerchantRepo
import com.sonnt.fp_be.repos.product.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class MerchantProductService: BaseMerchantService() {
    @Autowired
    lateinit var productRepo: ProductRepo
    @Autowired
    lateinit var productCategoryRepo: ProductCategoryRepo
    @Autowired
    lateinit var productTagRepo: ProductTagRepo

    fun getProductByMerchantId(merchantId: Long?, page: Int? = 0, size: Int? = 20): List<ProductDTO> {
        val pageable = PageRequest.of(page ?: 0, size ?: 20)
        return productRepo.findProductByMerchantId(merchantId, pageable).toProductDTO()
    }

    fun addProduct(productDTO: ProductDTO): ProductDTO {

        checkValidFoodParams(productDTO)

        val product = productDTO.toDbModel()

        val result = productRepo.save(product)
        productRepo.flush()

        return result.toDTO()
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

        checkValidMerchant(merchantId)

        if(!merchantRepo.existsById(merchantId))
            throw BusinessException(FPResponseStatus.merchantNotFound)

        if (!productCategoryRepo.existsById(productDTO.categoryId ?: 0))
            throw BusinessException(FPResponseStatus.categoryNotFound)

        if (productDTO.tagId != null && !productTagRepo.existsById(productDTO.tagId!!))
            throw BusinessException(FPResponseStatus.tagNotFound)

    }

}