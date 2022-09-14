package com.sonnt.fp_be.features.merchant.tag

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.merchant.BaseMerchantService
import com.sonnt.fp_be.features.shared.dto.ProductTagDTO
import com.sonnt.fp_be.model.entities.extension.toDTO
import com.sonnt.fp_be.model.entities.extension.toProductTagDTO
import com.sonnt.fp_be.repos.product.ProductRepo
import com.sonnt.fp_be.repos.product.ProductTagRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MerchantTagService: BaseMerchantService() {
    @Autowired
    lateinit var tagRepo: ProductTagRepo
    @Autowired
    lateinit var productRepo: ProductRepo

    fun getMerchantTag(merchantId: Long, page: Int?, size: Int?): List<ProductTagDTO> {
        val pageable = PageRequest.of(page ?: 0, size ?: 20)
        return tagRepo.findTagByMerchantId(merchantId, pageable).toProductTagDTO()
    }

    fun addTag(productTagDTO: ProductTagDTO): ProductTagDTO {

        checkValidMerchant(productTagDTO.merchantId)

        if (!merchantRepo.existsById(productTagDTO.merchantId ?: 0))
            throw BusinessException(FPResponseStatus.merchantNotFound)

        val result = tagRepo.save(productTagDTO.toDb())
        tagRepo.flush()
        return result.toDTO()
    }

    fun updateTag(productTagDTO: ProductTagDTO) {
        checkValidMerchant(productTagDTO.merchantId)

        tagRepo.save(productTagDTO.toDb())
        tagRepo.flush()
    }

    @Transactional(rollbackFor = [Exception::class, Throwable::class])
    fun deleteTag(tagId: Long?) {
        val tag = tagRepo.findById(tagId ?: 0)
            .orElseThrow { BusinessException(FPResponseStatus.tagNotFound) }

        productRepo.clearTag(tag.id)
        tagRepo.delete(tag)
        productRepo.flush()
        tagRepo.flush()
    }
}