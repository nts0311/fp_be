package com.sonnt.fp_be.features.auth.services

import com.sonnt.fp_be.model.entities.Account
import com.sonnt.fp_be.model.entities.Customer
import com.sonnt.fp_be.repos.AccountRepo
import com.sonnt.fp_be.repos.CustomerRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomerService {
    @Autowired
    lateinit var customerRepo: CustomerRepo
    @Autowired
    lateinit var accountRepo: AccountRepo
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    fun registerCustomer(account: Account) {
        account.password = passwordEncoder.encode(account.password)
        account.role = "CUSTOMER"
        val customer = Customer(account = account)
        customerRepo.save(customer)
        customerRepo.flush()
    }
}