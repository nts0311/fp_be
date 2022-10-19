package com.sonnt.fp_be.features.driver.order

import com.sonnt.fp_be.features.driver.order.dto.*
import com.sonnt.fp_be.features.enduser.order.EUOrderService
import com.sonnt.fp_be.features.shared.services.BaseService
import com.sonnt.fp_be.features.shared.services.FindDriverService
import com.sonnt.fp_be.features.shared.services.OrderTrackingService
import com.sonnt.fp_be.model.entities.DriverStatus
import com.sonnt.fp_be.model.entities.order.OrderStatus
import com.sonnt.fp_be.repos.DriverRepo
import com.sonnt.fp_be.repos.OrderRecordRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DriverOrderService: BaseService() {

    @Autowired lateinit var findDriverService: FindDriverService
    @Autowired lateinit var driverRepo: DriverRepo
    @Autowired lateinit var orderRepo: OrderRecordRepo
    @Autowired lateinit var orderTrackingService: OrderTrackingService
    @Autowired lateinit var euOrderService: EUOrderService

    @Transactional(rollbackFor = [Exception::class, Throwable::class])
    fun acceptOrder(orderId: Long) {
        val driverId = driverRepo.findDriverIdByUserId(userId)
        findDriverService.acceptOrderDeliveryRequest(orderId, driverId)

        val order = orderRepo.findById(orderId).get()
        val driver = driverRepo.findById(driverId).get()
        driver.status = DriverStatus.DELIVERING

        order.driver = driver
        order.status = OrderStatus.PREPARING

        driverRepo.save(driver)
        driverRepo.flush()
        orderRepo.save(order)
        orderRepo.flush()

        val orderInfo = euOrderService.getOrderInfo(order)
        val merchant = order.merchant ?: return

        orderTrackingService.sendNewOrderRequestToMerchant(merchant, orderInfo)
        orderTrackingService.sendSuccessFindingDriver(order)
    }

    fun arrivedAtMerchant(body: ArrivedAtMerchantRequest) {
        val orderId = body.orderId
        val order = orderRepo.findById(orderId).get()

        order.status = OrderStatus.PICKING_UP

        orderRepo.save(order)
        orderRepo.flush()

        orderTrackingService.sendDriverArrivedToMerchant(order)
    }

    fun confirmReceived(body: ConfirmReceivedOrderRequest) {
        val orderId = body.orderId
        val order = orderRepo.findById(orderId).get()

        order.billImageUrl = body.billImageUrl
        order.status = OrderStatus.DELIVERING

        orderRepo.save(order)
        orderRepo.flush()

        orderTrackingService.sendDriverDeliveringToCustomer(order)
    }

    fun arrivedAtCustomer(body: ArrivedAtCustomerRequest) {
        val orderId = body.orderId
        val order = orderRepo.findById(orderId).get()

        orderTrackingService.sendDriverArrivedToCustomer(order)
    }

    fun confirmCompletedOrder(body: ConfirmCompletedOrderRequest) {
        val orderId = body.orderId
        val order = orderRepo.findById(orderId).get()

        order.deliveredEvidenceImageUrl = body.evidenceImageUrl
        order.status = OrderStatus.SUCCEED

        orderRepo.save(order)
        orderRepo.flush()

        val driverId = driverRepo.findDriverIdByUserId(userId)
        val driver = driverRepo.findById(driverId).get()
        driver.status = DriverStatus.IDLE
        driverRepo.save(driver)
        driverRepo.flush()

        orderTrackingService.sendOrderCompleted(order)
    }


}