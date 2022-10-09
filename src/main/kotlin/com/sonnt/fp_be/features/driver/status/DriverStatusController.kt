package com.sonnt.fp_be.features.driver.status

import com.sonnt.fp_be.features.driver.order.dto.AcceptOrderResultRequest
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/driver/status")
class DriverStatusController: BaseController() {

    @Autowired
    lateinit var orderService: DriverStatusService

    @PostMapping("update-location")
    fun acceptOrder(@RequestBody body: FPAddressDTO): ResponseEntity<*> {
        orderService.updateLastLocation(body)
        return ok()
    }
}