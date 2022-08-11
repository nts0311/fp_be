package com.sonnt.fp_be.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.InvalidClaimException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.sonnt.fp_be.model.entities.AppUser
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtUtils {
    private val EXPRIRE_TIME = 5000
    private val verifier = JWT.require(Algorithm.HMAC256(SECRET.toByteArray())).build()

    fun extractUserId(token: String): Long {
        val decodedJwt = verifier.verify(token)
        return decodedJwt.claims["userId"]?.asLong() ?: throw InvalidClaimException("Cannot extract userId")
    }
    fun verifyJwtToken(token: String): DecodedJWT? {
        return try {
            verifier.verify(token)
        } catch (e: JWTVerificationException) {
            null
        }
    }
    fun getSecurityAuthToken(jwt: String): UsernamePasswordAuthenticationToken? {
        return try {
            val decodedJwt = verifier.verify(jwt)
            val username = decodedJwt.subject
            val userId = decodedJwt.claims["userId"]?.asLong() ?: 0
            val authorities = listOf(SimpleGrantedAuthority("USER"))
            val authToken = UsernamePasswordAuthenticationToken(username, null, authorities)
            authToken.details = userId

            authToken
        } catch (e: JWTVerificationException) {
            e.printStackTrace()
            null
        }
    }

    fun generateToken(user: AppUser): String {
        return JWT.create()
            .withSubject(user.username)
            .withClaim("userId", user.id)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPRIRE_TIME * 60 * 1000))
            .sign(Algorithm.HMAC256(SECRET))
    }

    companion object {
        val SECRET = "AbCd123"
    }
}