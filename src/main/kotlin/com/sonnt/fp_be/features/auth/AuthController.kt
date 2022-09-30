package com.sonnt.fp_be.features.auth

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.features.auth.response.AuthRequest
import com.sonnt.fp_be.features.auth.response.RegisterRequest
import com.sonnt.fp_be.features.shared.request.UpdateFcmTokenRequest
import com.sonnt.fp_be.features.auth.response.AuthenticationResponse
import com.sonnt.fp_be.features.auth.services.CustomerService
import com.sonnt.fp_be.features.auth.services.DriverAuthService
import com.sonnt.fp_be.features.auth.services.MerchantAuthService
import com.sonnt.fp_be.model.entities.Account
import com.sonnt.fp_be.utils.JwtUtils
import com.sonnt.fp_be.utils.badRequest
import com.sonnt.fp_be.utils.ok
import com.sonnt.fp_be.utils.unauthorized
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController @Autowired constructor(
    val authenticationManager: AuthenticationManager,
    val jwtUtils: JwtUtils,
    val customerService: CustomerService,
    val merchantAuthService: MerchantAuthService,
    val driverAuthService: DriverAuthService
) : BaseController() {
    @PostMapping("login")
    fun login(@RequestBody authRequest: AuthRequest): ResponseEntity<*> {
        if (!accountService.isUserExist(authRequest.username)) {
            throw BusinessException(FPResponseStatus.usernameNotFound)
        }

        try {
            val authToken = UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
            authenticationManager.authenticate(authToken)
            val user = accountService.getUserByUsername(authRequest.username)
            val jwtToken = jwtUtils.generateToken(user!!)
            return ok(AuthenticationResponse(jwtToken, user.id))
        }
        catch (e: BadCredentialsException){
            throw BusinessException(FPResponseStatus.incorrectPassword)
        }
    }

    @PostMapping("customer/register")
    fun registerCustomer(@RequestBody registerRequest: RegisterRequest): ResponseEntity<*> {
        if (accountService.isUserExist(registerRequest.username))
            return badRequest("MSG_USER_EXIST")

        val newUser = modelMapper.map(registerRequest, Account::class.java)
        customerService.registerCustomer(newUser)

        return ok()
    }

    @PostMapping("merchant/register")
    fun registerMerchant(@RequestBody registerRequest: RegisterRequest): ResponseEntity<*> {
        if (accountService.isUserExist(registerRequest.username))
            return badRequest("MSG_USER_EXIST")

        val newUser = modelMapper.map(registerRequest, Account::class.java)
        merchantAuthService.registerMerchant(newUser)

        return ok()
    }

    @PostMapping("driver/register")
    fun registerDriver(@RequestBody registerRequest: RegisterRequest): ResponseEntity<*> {
        if (accountService.isUserExist(registerRequest.username))
            return badRequest("MSG_USER_EXIST")

        val newUser = modelMapper.map(registerRequest, Account::class.java)

        driverAuthService.registerDriver(newUser)
        return ok()
    }

    @PostMapping("update-fcm-token")
    fun updateFcmToken(@RequestBody body: UpdateFcmTokenRequest): ResponseEntity<*> {
        accountService.updateFcmToken(userId, body.fcmToken)
        return ok()
    }
}