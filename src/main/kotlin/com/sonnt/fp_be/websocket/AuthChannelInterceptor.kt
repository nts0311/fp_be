package com.sonnt.fp_be.websocket

import com.sonnt.fp_be.utils.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Component


@Component
class AuthChannelInterceptor: ChannelInterceptor {

    @Autowired
    private lateinit var jwtUtils: JwtUtils

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)

        accessor?.also {accessor ->
            if (accessor.command == StompCommand.CONNECT) {
                val authHeader = accessor.getNativeHeader(HttpHeaders.AUTHORIZATION)?.first()
                val jwt = authHeader?.substring("Bearer ".length) ?: ""

                val securityToken = jwtUtils.getSecurityAuthToken(jwt) ?: throw BadCredentialsException("Invalid credentials")
                accessor.user = securityToken
            }
        }

        return message
    }
}