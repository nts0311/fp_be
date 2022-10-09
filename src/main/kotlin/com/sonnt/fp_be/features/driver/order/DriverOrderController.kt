package com.sonnt.fp_be.features.driver.order

import com.sonnt.fp_be.features.driver.order.dto.AcceptOrderResultRequest
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/driver/order")
class DriverOrderController: BaseController() {

    @Autowired
    lateinit var orderService: DriverOrderService

    @PostMapping("accept-order")
    fun acceptOrder(@RequestParam body: AcceptOrderResultRequest): ResponseEntity<*> {
        orderService.acceptOrder(body.orderId)
        return ok()
    }
}