package com.sonnt.fp_be.features.merchant

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.shared.services.BaseService
import com.sonnt.fp_be.repos.MerchantRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BaseMerchantService: BaseService() {

    @Autowired
    lateinit var merchantRepo: MerchantRepo

    val currentMerchantId: Long
        get() = merchantRepo.findMerchantByAccountId(userId).id

    @Throws(BusinessException::class)
    fun checkValidMerchant(merchantId: Long?) {
        if(currentMerchantId != merchantId)
            throw BusinessException(FPResponseStatus.unauthorized)
    }
}