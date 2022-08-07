package com.sonnt.fp_be.filters

import com.fasterxml.jackson.databind.ObjectMapper
import com.sonnt.fp_be.model.dto.response.BaseResponse
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ExceptionFilter: OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        }
        catch (e: Exception){
            e.printStackTrace()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            ObjectMapper().writeValue(response.outputStream, BaseResponse(-1, e.cause?.message ?: "An error occurred"))
        }
    }
}