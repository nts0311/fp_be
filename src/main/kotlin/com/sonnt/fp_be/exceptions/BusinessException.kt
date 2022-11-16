package com.sonnt.fp_be.exceptions

import com.sonnt.fp_be.model.dto.response.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


enum class FPResponseStatus(val code: String = "0", val httpStatus: HttpStatus = HttpStatus.OK, val message: String = "") {
    ok(httpStatus = HttpStatus.OK),
    notFoung(httpStatus = HttpStatus.NOT_FOUND),
    badRequest(httpStatus = HttpStatus.BAD_REQUEST),
    unauthorized(httpStatus = HttpStatus.UNAUTHORIZED, message = "'Không có quyền"),
    systemError(code = "-1", httpStatus = HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống"),

    merchantNotFound(code = "1", httpStatus = HttpStatus.NOT_FOUND, "Không tìm thấy nhà hàng"),
    categoryNotFound(code = "2", httpStatus = HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"),
    tagNotFound(code = "3", httpStatus = HttpStatus.NOT_FOUND, "Không tìm thấy tag"),
    productNotFound(code = "4", httpStatus = HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"),

    usernameNotFound(code = "5", httpStatus = HttpStatus.BAD_REQUEST, "Sai tên đăng nhập"),
    incorrectPassword(code = "6", httpStatus = HttpStatus.BAD_REQUEST, "Sai mật khẩu"),

    cartEmpty(code = "7", httpStatus = HttpStatus.BAD_REQUEST, "Đơn hàng trống"),

    merchantClosed(code = "8", httpStatus = HttpStatus.BAD_REQUEST, "Nhà hàng đã đóng cửa"),
    productNotAvailable(code = "9", httpStatus = HttpStatus.BAD_REQUEST, "Sản phẩm tạm hết"),
    userExisted(code = "10", httpStatus = HttpStatus.BAD_REQUEST, "Tên người dùng đã tồn tại"),

}

class BusinessException(
    private var responseStatus: FPResponseStatus = FPResponseStatus.badRequest
): RuntimeException() {
    fun toResponse(): ResponseEntity<*> {
        return ResponseEntity.status(responseStatus.httpStatus)
            .body(BaseResponse(responseStatus.httpStatus.value(), responseStatus.code, responseStatus.message))
    }
}