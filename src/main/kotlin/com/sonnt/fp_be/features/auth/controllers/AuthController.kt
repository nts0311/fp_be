package com.sonnt.fp_be.features.auth.controllers

import com.sonnt.fp_be.controllers.BaseController
import com.sonnt.fp_be.features.auth.dto.AuthRequest
import com.sonnt.fp_be.features.auth.dto.RegisterRequest
import com.sonnt.fp_be.model.dto.request.UpdateFcmTokenRequest
import com.sonnt.fp_be.features.auth.dto.AuthenticationResponse
import com.sonnt.fp_be.model.entities.AppUser
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
    val jwtUtils: JwtUtils
) : BaseController() {
    @PostMapping("login")
    fun login(@RequestBody authRequest: AuthRequest): ResponseEntity<*> {
        return try {
            val authToken = UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
            authenticationManager.authenticate(authToken)
            val user = userService.getUserByUsername(authRequest.username)
            val jwtToken = jwtUtils.generateToken(user!!)
            ok(AuthenticationResponse(jwtToken, user.id))
        }
        catch (e: BadCredentialsException){
            unauthorized()
        }
    }

    @PostMapping("register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<*> {
        if (userService.isUserExist(registerRequest.username))
            return badRequest("MSG_USER_EXIST")

        val newUser = modelMapper.map(registerRequest, AppUser::class.java)
        userService.saveUser(newUser)
        userService.flush()

        return ok()
    }

    @PostMapping("update-fcm-token")
    fun updateFcmToken(@RequestBody body: UpdateFcmTokenRequest): ResponseEntity<*> {
        userService.updateFcmToken(userId, body.fcmToken)
        return ok()
    }
}