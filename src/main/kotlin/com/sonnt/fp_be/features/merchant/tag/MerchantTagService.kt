package com.sonnt.fp_be.features.merchant.tag

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.merchant.BaseMerchantService
import com.sonnt.fp_be.features.shared.dto.ProductMenuDTO
import com.sonnt.fp_be.model.entities.extension.toDTO
import com.sonnt.fp_be.model.entities.extension.toProductTagDTO
import com.sonnt.fp_be.repos.product.ProductRepo
import com.sonnt.fp_be.repos.product.ProductMenuRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MerchantTagService: BaseMerchantService() {
    @Autowired
    lateinit var productMenuRepo: ProductMenuRepo
    @Autowired
    lateinit var productRepo: ProductRepo

    fun getMerchantTag(merchantId: Long, page: Int?, size: Int?): List<ProductMenuDTO> {
        val pageable = PageRequest.of(page ?: 0, size ?: 20)
        return productMenuRepo.findTagByMerchantId(merchantId, pageable).toProductTagDTO()
    }

    fun getProductMenu(): List<ProductMenuDTO> {
        val pageable = PageRequest.of( 0, 100)
        return productMenuRepo.findTagByMerchantId(currentMerchantId, pageable).toProductTagDTO()
    }

    fun addTag(productTagDTO: ProductMenuDTO): ProductMenuDTO {

        checkValidMerchant(currentMerchantId)

        if (!merchantRepo.existsById(currentMerchantId ?: 0))
            throw BusinessException(FPResponseStatus.merchantNotFound)

        productTagDTO.merchantId = currentMerchantId

        val result = productMenuRepo.save(productTagDTO.toDb())
        productMenuRepo.flush()
        return result.toDTO()
    }

    fun updateTag(productTagDTO: ProductMenuDTO) {
        checkValidMerchant(currentMerchantId)

        productTagDTO.merchantId = currentMerchantId

        productMenuRepo.save(productTagDTO.toDb())
        productMenuRepo.flush()
    }

    @Transactional(rollbackFor = [Exception::class, Throwable::class])
    fun deleteTag(tagId: Long?) {
        val tag = productMenuRepo.findById(tagId ?: 0)
            .orElseThrow { BusinessException(FPResponseStatus.tagNotFound) }

        productRepo.clearTag(tag.id)
        productMenuRepo.delete(tag)
        productRepo.flush()
        productMenuRepo.flush()
    }
}