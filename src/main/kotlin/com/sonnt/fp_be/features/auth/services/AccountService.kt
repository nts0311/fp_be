package com.sonnt.fp_be.features.auth.services

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.auth.response.AuthRequest
import com.sonnt.fp_be.features.auth.response.AuthResponse
import com.sonnt.fp_be.features.auth.response.RegisterRequest
import com.sonnt.fp_be.features.shared.services.BaseService
import com.sonnt.fp_be.model.entities.Account
import com.sonnt.fp_be.model.entities.Customer
import com.sonnt.fp_be.model.entities.Driver
import com.sonnt.fp_be.model.entities.Merchant
import com.sonnt.fp_be.repos.AccountRepo
import com.sonnt.fp_be.repos.CustomerRepo
import com.sonnt.fp_be.repos.DriverRepo
import com.sonnt.fp_be.repos.MerchantRepo
import com.sonnt.fp_be.utils.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class AccountService: BaseService() {
    @Autowired lateinit var passwordEncoder: PasswordEncoder
    @Autowired lateinit var authenticationManager: AuthenticationManager
    @Autowired lateinit var jwtUtils: JwtUtils
    @Autowired lateinit var accountRepo: AccountRepo
    @Autowired lateinit var customerRepo: CustomerRepo
    @Autowired lateinit var driverRepo: DriverRepo
    @Autowired lateinit var merchantRepo: MerchantRepo
    fun updateFcmToken(fcmToken: String) {
        val user = accountRepo.findById(userId).get()
        user.fcmToken = fcmToken
        accountRepo.save(user)
    }
    fun registerCustomer(registerRequest: RegisterRequest) {
        checkUsernameExisted(registerRequest.username)
        val account = getAccountWithRole(registerRequest, "CUSTOMER")
        val customer = Customer(account = account)
        customerRepo.save(customer)
        customerRepo.flush()
    }
    fun registerDriver(registerRequest: RegisterRequest) {
        checkUsernameExisted(registerRequest.username)
        val account = getAccountWithRole(registerRequest, "DRIVER")
        val driver = Driver(account = account)
        driverRepo.save(driver)
        driverRepo.flush()
    }

    fun registerMerchant(registerRequest: RegisterRequest) {
        checkUsernameExisted(registerRequest.username)
        val account = getAccountWithRole(registerRequest, "MERCHANT")
        val merchant = Merchant(account = account, name = account.name)
        merchantRepo.save(merchant)
        merchantRepo.flush()
    }

    fun login(@RequestBody authRequest: AuthRequest): AuthResponse {
        if (!isUserExist(authRequest.username)) {
            throw BusinessException(FPResponseStatus.usernameNotFound)
        }

        try {
            val authToken = UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
            authenticationManager.authenticate(authToken)
            return getAuthResponse(authRequest.username)
        }
        catch (e: BadCredentialsException){
            throw BusinessException(FPResponseStatus.incorrectPassword)
        }
    }

    private fun isUserExist(username: String): Boolean = accountRepo.findByUsername(username) != null

    private fun getAuthResponse(username: String): AuthResponse {
        val user = accountRepo.findByUsername(username)
        val jwtToken = jwtUtils.generateToken(user!!)

        return AuthResponse(jwtToken = jwtToken, userId = user.id, name = user.name, avatarUrl = user.avatarUrl)
    }

    private fun checkUsernameExisted(username: String) {
        if (isUserExist(username))
            throw BusinessException(FPResponseStatus.userExisted)
    }
    private fun getAccountWithRole(registerRequest: RegisterRequest, role: String): Account {
        return modelMapper.map(registerRequest, Account::class.java).apply {
            password = passwordEncoder.encode(this.password)
            this.role = role
        }
    }
}