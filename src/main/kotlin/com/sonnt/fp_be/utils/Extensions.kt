package com.sonnt.fp_be.utils

import com.sonnt.fp_be.model.dto.response.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun badRequest(msg: String = "")
    = ResponseEntity.badRequest().body(BaseResponse(code = -1, msg = msg))

fun unauthorized()
        = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse(code = -1, msg = "MSG_INCORRECT_AUTH_INFO"))

fun ok() = ResponseEntity.ok().body(BaseResponse(code = 0, msg = "success"))

fun ok(body: Any)
        = ResponseEntity.ok().body(BaseResponse(code = 0, msg = "success", data = body))