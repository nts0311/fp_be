package com.sonnt.fp_be.model.dto.response

import org.springframework.http.HttpStatus

open class BaseResponse(
    var httpStatus: Int = HttpStatus.OK.value(),
    var code: String = "0",
    var msg: String = "Success",
) {

}
