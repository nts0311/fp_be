package com.sonnt.fp_be.features.enduser.order

import com.sonnt.fp_be.features.enduser.order.request.CreateOrderRequest
import com.sonnt.fp_be.features.enduser.order.request.GetOrderCheckinInfoRequest
import com.sonnt.fp_be.features.enduser.order.response.CreateNewOrderResponse
import com.sonnt.fp_be.features.enduser.order.response.GetOrderCheckinInfoResponse
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enduser/order")
class EUOrderController: BaseController() {

    @Autowired lateinit var orderService: EUOrderService

    @PostMapping("checkin-info")
    fun getOrderCheckinInfo(@RequestBody body: GetOrderCheckinInfoRequest): ResponseEntity<*> {
        val result = orderService.getOrderCheckinInfo(body)
        return ok(GetOrderCheckinInfoResponse(result))
    }

    @PostMapping("create")
    fun createOrder(@RequestBody body: CreateOrderRequest): ResponseEntity<*> {
        val orderId = orderService.createOrder(body)
        return ok(CreateNewOrderResponse(orderId))
    }

    @GetMapping("get-info")
    fun getOrderInfo(@RequestParam orderId: Long): ResponseEntity<*> {
        return ok(orderService.getOrderInfo(orderId))
    }
}