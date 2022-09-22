package com.sonnt.fp_be.features.enduser.order

import com.sonnt.fp_be.features.enduser.order.request.GetOrderCheckinInfoRequest
import com.sonnt.fp_be.features.enduser.order.response.GetOrderCheckinInfoResponse
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/enduser/order")
class EUOrderController: BaseController() {

    @Autowired lateinit var orderService: EUOrderService

    @PostMapping("checkin-info")
    fun getEstimatedTimeFromMerchantToCustomer(@RequestBody body: GetOrderCheckinInfoRequest): ResponseEntity<*> {
        val result = orderService.getOrderCheckinInfo(body)
        return ok(GetOrderCheckinInfoResponse(result))
    }
}