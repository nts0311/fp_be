package com.sonnt.fp_be.features.auth.dto

data class RegisterRequest(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val name: String = ""
)
