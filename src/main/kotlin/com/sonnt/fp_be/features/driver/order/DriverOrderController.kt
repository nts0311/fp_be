package com.sonnt.fp_be.features.driver.order

import com.sonnt.fp_be.features.driver.order.dto.*
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.utils.badRequest
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/driver/order")
class DriverOrderController: BaseController() {

    @Autowired
    lateinit var orderService: DriverOrderService

    @PostMapping("accept-order")
    fun acceptOrder(@RequestBody body: AcceptOrderResultRequest): ResponseEntity<*> {
        val orderId = body.orderId ?: return badRequest("Invaild parrams")
        orderService.acceptOrder(orderId)
        return ok()
    }

    @PostMapping("arrived-at-merchant")
    fun arrivedAtMerchant(@RequestBody body: ArrivedAtMerchantRequest): ResponseEntity<*> {
        orderService.arrivedAtMerchant(body)
        return ok()
    }

    @PostMapping("confirm-received-order")
    fun confirmReceivedOrder(@RequestBody body: ConfirmReceivedOrderRequest): ResponseEntity<*> {
        orderService.confirmReceived(body)
        return ok()
    }

    @PostMapping("arrived-at-customer")
    fun arrivedAtCustomer(@RequestBody body: ArrivedAtCustomerRequest): ResponseEntity<*> {
        orderService.arrivedAtCustomer(body)
        return ok()
    }

    @PostMapping("confirm-completed-order")
    fun confirmCompletedOrder(@RequestBody body: ConfirmCompletedOrderRequest): ResponseEntity<*> {
        orderService.confirmCompletedOrder(body)
        return ok()
    }
}