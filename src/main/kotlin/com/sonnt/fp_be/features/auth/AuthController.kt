package com.sonnt.fp_be.features.auth

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.features.auth.response.AuthRequest
import com.sonnt.fp_be.features.auth.response.RegisterRequest
import com.sonnt.fp_be.features.auth.response.UpdateFcmTokenRequest
import com.sonnt.fp_be.features.auth.response.AuthResponse
import com.sonnt.fp_be.features.auth.services.AccountService
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController @Autowired constructor(
    val accountService: AccountService
) : BaseController() {
    @PostMapping("login")
    fun login(@RequestBody authRequest: AuthRequest): ResponseEntity<*> {
        return ok(accountService.login(authRequest))
    }

    @PostMapping("customer/register")
    fun registerCustomer(@RequestBody registerRequest: RegisterRequest): ResponseEntity<*> {
        accountService.registerCustomer(registerRequest)
        return ok()
    }

    @PostMapping("merchant/register")
    fun registerMerchant(@RequestBody registerRequest: RegisterRequest): ResponseEntity<*> {
        accountService.registerMerchant(registerRequest)
        return ok()
    }

    @PostMapping("driver/register")
    fun registerDriver(@RequestBody registerRequest: RegisterRequest): ResponseEntity<*> {
        accountService.registerDriver(registerRequest)
        return ok()
    }

    @PostMapping("update-fcm-token")
    fun updateFcmToken(@RequestBody body: UpdateFcmTokenRequest): ResponseEntity<*> {
        accountService.updateFcmToken(body.fcmToken)
        return ok()
    }
}