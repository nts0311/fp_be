package com.sonnt.fp_be.exceptions

import com.sonnt.fp_be.model.dto.response.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionsHandler {
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessExceptions(ex: BusinessException): ResponseEntity<*> {
        return ex.toResponse()
    }

    @ExceptionHandler(Exception::class)
    fun handleExceptions(ex: Exception): ResponseEntity<*> {
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse(ex.message ?: "Lỗi hệ thống", ex.stackTrace.toString()))
    }
}