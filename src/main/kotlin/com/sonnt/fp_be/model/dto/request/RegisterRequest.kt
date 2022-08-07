package com.sonnt.fp_be.model.dto.request

data class RegisterRequest(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val name: String = ""
)
