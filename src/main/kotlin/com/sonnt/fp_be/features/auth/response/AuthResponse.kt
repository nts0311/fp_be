package com.sonnt.fp_be.features.auth.response

import com.sonnt.fp_be.model.dto.response.BaseResponse

class AuthResponse(
    val jwtToken: String? = null,
    val userId: Long = 0L,
    val name: String? = null,
    val avatarUrl: String? = null
): BaseResponse()