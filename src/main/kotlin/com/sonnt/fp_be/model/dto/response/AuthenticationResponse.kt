package com.sonnt.fp_be.model.dto.response

class AuthenticationResponse(
    val jwtToken: String? = null,
    val userId: Long = 0L
): BaseResponse()