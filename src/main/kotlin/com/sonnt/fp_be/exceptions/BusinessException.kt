package com.sonnt.fp_be.exceptions

import com.sonnt.fp_be.model.dto.response.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


enum class FPResponseStatus(val code: String = "0", val httpStatus: HttpStatus = HttpStatus.OK, val message: String = "") {
    ok(httpStatus = HttpStatus.OK),
    notFoung(httpStatus = HttpStatus.NOT_FOUND),
    badRequest(httpStatus = HttpStatus.BAD_REQUEST),

    merchantNotFound(code = "1", httpStatus = HttpStatus.NOT_FOUND, "Không tìm thấy nhà hàng"),
    categoryNotFound(code = "2", httpStatus = HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"),
    tagNotFound(code = "3", httpStatus = HttpStatus.NOT_FOUND, "Không tìm thấy tag")
}

class BusinessException(
    private var responseStatus: FPResponseStatus = FPResponseStatus.badRequest
): RuntimeException() {
    fun toResponse(): ResponseEntity<*> {
        return ResponseEntity.status(responseStatus.httpStatus)
            .body(BaseResponse(responseStatus.httpStatus.value(), responseStatus.code, responseStatus.message))
    }
}