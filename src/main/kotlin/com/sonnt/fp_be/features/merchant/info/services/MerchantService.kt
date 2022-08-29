package com.sonnt.fp_be.features.merchant.info.services

import com.sonnt.fp_be.features.base.services.BaseService
import com.sonnt.fp_be.repos.MerchantRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MerchantService: BaseService() {
    @Autowired lateinit var merchantRepo: MerchantRepo

    fun isMerchantExist(id: Long?) = merchantRepo.existsById(id ?: -1)
}