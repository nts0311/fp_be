package com.sonnt.fp_be.features.merchant.tag

import com.sonnt.fp_be.features.shared.services.BaseService
import com.sonnt.fp_be.repos.product.ProductTagRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MerchantTagService: BaseService() {
    @Autowired
    lateinit var tagRepo: ProductTagRepo

    fun isTagExists(tagId: Long?) = tagRepo.existsById(tagId ?: -1)
}