package com.sonnt.fp_be.features.auth.dto

import com.sonnt.fp_be.model.dto.response.BaseResponse

class AuthenticationResponse(
    val jwtToken: String? = null,
    val userId: Long = 0L
): BaseResponse()