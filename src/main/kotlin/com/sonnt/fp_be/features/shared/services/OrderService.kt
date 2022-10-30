package com.sonnt.fp_be.features.shared.services

import com.sonnt.fp_be.model.entities.DriverStatus
import com.sonnt.fp_be.model.entities.order.OrderRecord
import com.sonnt.fp_be.model.entities.order.OrderStatus
import com.sonnt.fp_be.repos.DriverRepo
import com.sonnt.fp_be.repos.OrderRecordRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService: BaseService() {

    @Autowired
    lateinit var driverRepo: DriverRepo
    @Autowired
    lateinit var orderRepo: OrderRecordRepo

    fun driverAcceptOrder(order: OrderRecord, driverId: Long) {

        val driver = driverRepo.findById(driverId).get()
        driver.status = DriverStatus.DELIVERING

        order.driver = driver
        order.status = OrderStatus.PREPARING
        order.driverAcceptTime = LocalDateTime.now()

        driverRepo.save(driver)
        driverRepo.flush()
        orderRepo.save(order)
        orderRepo.flush()
    }

    fun driverArrivedAtMerchant(order: OrderRecord) {
        order.status = OrderStatus.PICKING_UP
        orderRepo.save(order)
        orderRepo.flush()
    }

    fun driverConfirmReceiveOrderFromMerchant(order: OrderRecord, billImageUrl: String) {
        order.billImageUrl = billImageUrl
        order.status = OrderStatus.DELIVERING
        orderRepo.save(order)
        orderRepo.flush()
    }

    fun driverConfirmCompletedOrder(order: OrderRecord, evidenceImageUrl: String) {
        order.deliveredEvidenceImageUrl = evidenceImageUrl
        order.status = OrderStatus.SUCCEED

        orderRepo.save(order)
        orderRepo.flush()

        val driverId = driverRepo.findDriverIdByUserId(userId)
        val driver = driverRepo.findById(driverId).get()
        driver.status = DriverStatus.IDLE
        driverRepo.save(driver)
        driverRepo.flush()
    }

    fun cancelOrder(order: OrderRecord) {
        order.status = OrderStatus.CANCELED
        orderRepo.save(order)
        orderRepo.flush()
    }

}