package com.sonnt.fp_be.features.shared.model

enum class WSMessageCode(val code: Int) {
    FINDING_DRIVER(0),
    NEW_ORDER(1),
    FOUND_DRIVER(2),
    CANCEL_ORDER(3)
}

data class WSMessage(val code: Int, val body: String) {
    constructor(code: WSMessageCode, body: String) : this(code.code, body)
}

data class WSMessageWrapper<T>(
    val endpoint: String,
    val username: String,
    val code: WSMessageCode,
    val body: T
)