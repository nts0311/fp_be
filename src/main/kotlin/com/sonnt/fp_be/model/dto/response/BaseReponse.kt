package com.sonnt.fp_be.model.dto.response

open class BaseResponse(
    var code: Int = 0,
    var msg: String = "",
    var data: Any? = null
)
