package com.sonnt.fp_be.features.driver.order

import com.sonnt.fp_be.features.shared.services.BaseService
import com.sonnt.fp_be.features.shared.services.FindDriverService
import com.sonnt.fp_be.repos.DriverRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DriverOrderService: BaseService() {

    @Autowired lateinit var findDriverService: FindDriverService
    @Autowired lateinit var driverRepo: DriverRepo

    fun acceptOrder(orderId: Long) {
        val driverId = driverRepo.findDriverIdByUserId(userId)
        findDriverService.acceptOrderDeliveryRequest(orderId, driverId)
    }


}