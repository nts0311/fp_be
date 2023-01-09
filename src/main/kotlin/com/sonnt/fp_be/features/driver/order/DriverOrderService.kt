package com.sonnt.fp_be.features.driver.order

import com.sonnt.fp_be.features.driver.order.dto.*
import com.sonnt.fp_be.features.enduser.order.response.OrderInfo
import com.sonnt.fp_be.features.shared.services.*
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
    @Autowired lateinit var orderInfoService: OrderInfoService

    @Autowired lateinit var orderService: OrderService

    @Transactional(rollbackFor = [Exception::class, Throwable::class])
    fun acceptOrder(orderId: Long) {
        val driverId = driverRepo.findDriverIdByUserId(userId)
        findDriverService.acceptOrderDeliveryRequest(orderId, driverId)

        val order = orderRepo.findById(orderId).get()

        orderService.driverAcceptOrder(order, driverId)
        orderTrackingService.onSuccessFindingDriver(order)
    }

    fun arrivedAtMerchant(body: ArrivedAtMerchantRequest) {
        val orderId = body.orderId
        val order = orderRepo.findById(orderId).get()

        orderService.driverArrivedAtMerchant(order)
        orderTrackingService.sendDriverArrivedToMerchant(order)
    }

    fun confirmReceived(body: ConfirmReceivedOrderRequest) {
        val orderId = body.orderId
        val order = orderRepo.findById(orderId).get()

        orderService.driverConfirmReceiveOrderFromMerchant(order, body.billImageUrl)
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

        orderService.driverConfirmCompletedOrder(order, body.evidenceImageUrl)
        orderTrackingService.sendOrderCompleted(order)
    }

    fun getDriverActiveOrder(): OrderInfo? {
        val driverId = driverRepo.findDriverIdByUserId(userId)
        val order = orderRepo.findActiveOrderOfDriver(driverId) ?: return null
        return orderInfoService.getOrderInfo(order)
    }

    fun cancelOrder(orderId: Long) {
        val order = orderRepo.findById(orderId).get()
        orderService.cancelOrder(order)
        orderTrackingService.driverCanceledOrder(order)
        val driverId = driverRepo.findDriverIdByUserId(userId)
        val driver = driverRepo.findById(driverId).get()
        driver.status = DriverStatus.IDLE
        driverRepo.flush()

    }

}