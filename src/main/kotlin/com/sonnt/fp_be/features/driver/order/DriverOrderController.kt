package com.sonnt.fp_be.features.driver.order

import com.sonnt.fp_be.features.driver.order.dto.*
import com.sonnt.fp_be.features.enduser.order.response.GetActiveOrderResponse
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.utils.badRequest
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/driver/order")
class DriverOrderController: BaseController() {

    @Autowired
    lateinit var orderService: DriverOrderService

    @GetMapping("get-active-order")
    fun getActiveOrder(): ResponseEntity<*> {
        val orderInfo = orderService.getDriverActiveOrder()
        return ok(GetActiveOrderResponse(orderInfo))
    }

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

    @GetMapping("cancel-order")
    fun cancelOrder(@RequestParam("orderId") orderId: Long): ResponseEntity<*> {
        orderService.cancelOrder(orderId)
        return ok()
    }

}