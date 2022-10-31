package com.sonnt.fp_be.features.shared.services

import com.sonnt.fp_be.features.enduser.order.response.OrderInfo
import com.sonnt.fp_be.features.shared.model.WSMessageCode
import com.sonnt.fp_be.features.shared.model.WSMessageWrapper
import com.sonnt.fp_be.features.shared.model.toDTO
import com.sonnt.fp_be.model.entities.Driver
import com.sonnt.fp_be.model.entities.Merchant
import com.sonnt.fp_be.model.entities.extension.getCustomerUsername
import com.sonnt.fp_be.model.entities.extension.getDriverUsername
import com.sonnt.fp_be.model.entities.extension.getMerchantUsername
import com.sonnt.fp_be.model.entities.order.OrderRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderTrackingService: BaseService() {
    val WS_DRIVER_ORDER_DELIVERY_REQUEST = "/ws/driver/newOrderRequest"
    val WS_DRIVER_ORDER_STATUS = "/ws/driver/orderStatus"
    val WS_EU_ORDER_STATUS = "/ws/eu/orderStatus"
    val WS_MERCHANT_ORDER_STATUS = "/ws/merchant/orderStatus"
    val WS_MERCHANT_ORDERS = "/ws/merchant/newOrderRequest"

    @Autowired
    private lateinit var wsMessageService: WSMessageService

    @Autowired
    lateinit var orderInfoService: OrderInfoService

    fun onSuccessFindingDriver(order: OrderRecord) {
        val orderInfo = orderInfoService.getOrderInfo(order)
        val merchant = order.merchant ?: return
        sendNewOrderRequestToMerchant(merchant, orderInfo)
        sendSuccessFindingDriver(order)
    }

    fun sendNewOrderRequestToDriver(driver: Driver, orderInfo: OrderInfo) {
        println("Send new order request to: ${driver.id}")
        val message = WSMessageWrapper(WS_DRIVER_ORDER_DELIVERY_REQUEST,driver.account.username, WSMessageCode.NEW_ORDER, orderInfo)
        wsMessageService.sendMessage(message)
    }

    fun sendNewOrderRequestToMerchant(merchant: Merchant, orderInfo: OrderInfo) {
        val message = WSMessageWrapper(WS_MERCHANT_ORDERS, merchant.account.username, WSMessageCode.NEW_ORDER, orderInfo)
        wsMessageService.sendMessage(message)
    }

    fun sendSuccessFindingDriver(order: OrderRecord) {
        val customerUsername = order.getCustomerUsername() ?: return
        val driverDTO = order.driver?.toDTO() ?: return
        val message = WSMessageWrapper(WS_EU_ORDER_STATUS, customerUsername, WSMessageCode.FOUND_DRIVER, driverDTO)
        wsMessageService.sendMessage(message)
    }

    fun sendNotSuccessFindingDriver(order: OrderRecord) {
        sendOrderCanceledToCustomer(order, "Không tìm được tài xế")
    }

    fun sendDriverArrivedToMerchant(orderRecord: OrderRecord) {
        val body = DriverArrivedAtMerchantMessage(orderRecord.id, orderRecord.driver?.account?.name ?: "", orderRecord.driver?.plate ?: "")
        val merchantUsername = orderRecord.getMerchantUsername() ?: return
        val message = WSMessageWrapper(WS_MERCHANT_ORDER_STATUS, merchantUsername, WSMessageCode.DRIVER_ARRIVED_TO_MERCHANT, body)
        wsMessageService.sendMessage(message)
    }

    fun sendDriverDeliveringToCustomer(orderRecord: OrderRecord) {
        val customerUsername = orderRecord.getCustomerUsername() ?: return
        val message = WSMessageWrapper(WS_EU_ORDER_STATUS, customerUsername, WSMessageCode.DELIVERING_ORDER_TO_CUSTOMER, "")
        wsMessageService.sendMessage(message)
    }

    fun sendDriverArrivedToCustomer(orderRecord: OrderRecord) {
        val customerUsername = orderRecord.getCustomerUsername() ?: return
        val message = WSMessageWrapper(WS_EU_ORDER_STATUS, customerUsername, WSMessageCode.DRIVER_ARRIVED_TO_CUSTOMER, "")
        wsMessageService.sendMessage(message)
    }

    fun sendOrderCompleted(order: OrderRecord) {
        val customerUsername = order.getCustomerUsername()
        val merchantUsername = order.getCustomerUsername()

        customerUsername?.let {
            val messageToCustomer = WSMessageWrapper(WS_EU_ORDER_STATUS, customerUsername, WSMessageCode.ORDER_COMPLETED, "${order.id}")
            wsMessageService.sendMessage(messageToCustomer)
        }

        merchantUsername?.let {
            val messageToCustomer = WSMessageWrapper(WS_MERCHANT_ORDER_STATUS, merchantUsername, WSMessageCode.ORDER_COMPLETED, "${order.id}")
            wsMessageService.sendMessage(messageToCustomer)
        }
    }

    fun driverCanceledOrder(order: OrderRecord) {
        val reason = "Tài xế đã huỷ đơn hàng"
        sendOrderCanceledToCustomer(order, reason)
        sendOrderCanceledToMerchant(order, reason)
    }

    fun merchantCanceledOrder(order: OrderRecord) {
        val reason = "Nhà hàng đã huỷ đơn hàng"
        sendOrderCanceledToCustomer(order, reason)
        sendOrderCanceledToDriver(order, reason)
    }

    fun sendOrderCanceledToCustomer(order: OrderRecord, reason: String) {
        val customerUsername = order.getCustomerUsername()

        val body = CanceledOrderMessage(order.id, reason)

        customerUsername?.let {
            val messageToCustomer = WSMessageWrapper(WS_EU_ORDER_STATUS, customerUsername, WSMessageCode.CANCEL_ORDER, body)
            wsMessageService.sendMessage(messageToCustomer)
        }
    }


    fun sendOrderCanceledToDriver(order: OrderRecord, reason: String) {
        val driverUsername = order.getDriverUsername()

        val body = CanceledOrderMessage(order.id, reason)

        driverUsername?.let {
            val messageToCustomer = WSMessageWrapper(WS_DRIVER_ORDER_STATUS, driverUsername, WSMessageCode.CANCEL_ORDER, body)
            wsMessageService.sendMessage(messageToCustomer)
        }
    }

    fun sendOrderCanceledToMerchant(order: OrderRecord, reason: String) {
        val merchantUsername = order.getCustomerUsername()

        val body = CanceledOrderMessage(order.id, reason)

        merchantUsername?.let {
            val messageToCustomer = WSMessageWrapper(WS_MERCHANT_ORDER_STATUS, merchantUsername, WSMessageCode.CANCEL_ORDER, body)
            wsMessageService.sendMessage(messageToCustomer)
        }
    }


}

class CanceledOrderMessage(
    val id: Long,
    val reason: String
)

class DriverArrivedAtMerchantMessage(
    val orderId: Long,
    val driverName: String,
    val driverPlate: String
)
