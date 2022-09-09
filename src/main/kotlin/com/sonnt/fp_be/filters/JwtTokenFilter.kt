package com.sonnt.fp_be.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.fasterxml.jackson.databind.ObjectMapper
import com.sonnt.fp_be.model.dto.response.BaseResponse
import com.sonnt.fp_be.utils.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenFilter(private val jwtUtils: JwtUtils): OncePerRequestFilter() {
    private val shouldNotFilterPaths = listOf(
        "/auth/login",
        "/auth/register"
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (shouldNotFilter(request.servletPath)){
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            val jwt = authHeader.substring("Bearer ".length)
            val securityAuthToken = jwtUtils.getSecurityAuthToken(jwt)

            if (securityAuthToken != null) {
                SecurityContextHolder.getContext().authentication = securityAuthToken
                filterChain.doFilter(request, response)
            } else {
                response.status = HttpStatus.UNAUTHORIZED.value()
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                ObjectMapper().writeValue(response.outputStream, BaseResponse(httpStatus = HttpStatus.UNAUTHORIZED.value(), code = "-1", msg = "MSG_INVALID_TOKEN"))
            }
        }
        else {
            filterChain.doFilter(request, response)
        }
    }

    private fun shouldNotFilter(servletPath: String): Boolean = shouldNotFilterPaths.contains(servletPath)
}