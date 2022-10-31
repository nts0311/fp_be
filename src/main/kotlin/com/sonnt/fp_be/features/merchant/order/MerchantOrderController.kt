package com.sonnt.fp_be.features.merchant.order

import com.sonnt.fp_be.features.merchant.order.dto.GetMerchantActiveOrdersResponse
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/merchant/order")
class MerchantOrderController: BaseController() {

    @Autowired
    lateinit var orderService: MerchantOrderService

    @GetMapping("get-active-orders")
    fun getActiveOrders(): ResponseEntity<*> {
        val activeOrders = orderService.getActiveOrders()
        return ok(GetMerchantActiveOrdersResponse(activeOrders))
    }

    @GetMapping("cancel-order")
    fun cancelOrder(@RequestParam("orderId") orderId: Long): ResponseEntity<*> {
        orderService.cancelOrder(orderId)
        return ok()
    }

}
