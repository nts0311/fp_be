package com.sonnt.fp_be.features.auth.services

import com.sonnt.fp_be.model.entities.Account
import com.sonnt.fp_be.model.entities.Customer
import com.sonnt.fp_be.model.entities.Merchant
import com.sonnt.fp_be.repos.AccountRepo
import com.sonnt.fp_be.repos.CustomerRepo
import com.sonnt.fp_be.repos.MerchantRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MerchantAuthService {
    @Autowired
    lateinit var merchantRepo: MerchantRepo
    @Autowired
    lateinit var accountRepo: AccountRepo
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    fun registerMerchant(account: Account) {
        account.password = passwordEncoder.encode(account.password)
        account.role = "MERCHANT"
        val merchant = Merchant(account = account, name = account.name)
        merchantRepo.save(merchant)
        merchantRepo.flush()
    }
}