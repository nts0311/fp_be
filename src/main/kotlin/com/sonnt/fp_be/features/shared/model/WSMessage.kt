package com.sonnt.fp_be.features.shared.model

enum class WSMessageCode(val code: Int) {
    ORDER_COMPLETED(0),
    NEW_ORDER(1),
    FOUND_DRIVER(2),
    CANCEL_ORDER(3),
    DRIVER_ARRIVED_TO_MERCHANT(4),
    DELIVERING_ORDER_TO_CUSTOMER(5),
    DRIVER_ARRIVED_TO_CUSTOMER(6),
    DRIVER_LOCATION(7)
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