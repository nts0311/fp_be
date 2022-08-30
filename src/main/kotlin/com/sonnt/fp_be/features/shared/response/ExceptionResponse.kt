package com.sonnt.fp_be.features.shared.response

import com.sonnt.fp_be.model.dto.response.BaseResponse
import org.springframework.http.HttpStatus

class ExceptionResponse(
    message: String = "",
    stackTrace: String
): BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "500", "Lỗi hệ thống") {
}