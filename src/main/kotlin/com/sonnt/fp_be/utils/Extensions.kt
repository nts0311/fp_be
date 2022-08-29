package com.sonnt.fp_be.utils

import com.sonnt.fp_be.model.dto.response.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun badRequest(msg: String = "") =
    ResponseEntity.badRequest().body(BaseResponse(httpStatus = HttpStatus.BAD_REQUEST.value(), code = "-1", msg = msg))

fun unauthorized() = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    .body(BaseResponse(httpStatus = HttpStatus.UNAUTHORIZED.value(), code = "-1", msg = "MSG_INCORRECT_AUTH_INFO"))

fun notFound(message: String = "Not found") = ResponseEntity.status(HttpStatus.NOT_FOUND)
    .body(BaseResponse(httpStatus = HttpStatus.NOT_FOUND.value(), code = "404", msg = message))

fun ok() = ResponseEntity.ok().body(BaseResponse())

fun ok(body: Any) = ResponseEntity.ok().body(body)
