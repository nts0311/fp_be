package com.sonnt.fp_be.features.enduser

import com.sonnt.fp_be.features.shared.services.BaseService
import com.sonnt.fp_be.repos.AccountRepo
import com.sonnt.fp_be.repos.CustomerRepo
import org.springframework.beans.factory.annotation.Autowired


open class EndUserBaseService: BaseService() {
    @Autowired lateinit var customerRepo: CustomerRepo
    @Autowired lateinit var accountRepo: AccountRepo
}