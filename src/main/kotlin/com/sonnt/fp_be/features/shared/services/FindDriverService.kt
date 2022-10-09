package com.sonnt.fp_be.features.shared.services

import com.sonnt.fp_be.features.enduser.order.EUOrderService
import com.sonnt.fp_be.features.enduser.order.response.OrderInfo
import com.sonnt.fp_be.features.shared.model.WSMessageCode
import com.sonnt.fp_be.features.shared.model.WSMessageWrapper
import com.sonnt.fp_be.model.entities.Address
import com.sonnt.fp_be.model.entities.Driver
import com.sonnt.fp_be.model.entities.order.OrderRecord
import com.sonnt.fp_be.repos.DriverRepo
import com.sonnt.fp_be.repos.OrderRecordRepo
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

const val WS_DRIVER_ORDER_DELIVERY_REQUEST = "/ws/driver/newOrderRequest"
const val WS_DRIVER_ORDER_STATUS = "/users/driver/orderStatus"

const val WS_EU_ORDER_STATUS = "/users/eu/orderStatus"

const val WS_MERCHANT_ORDERS = "/users/merchant/orders"

//🥺
@Service
class FindDriverService {

    @Autowired private lateinit var orderRecordRepo: OrderRecordRepo
    @Autowired private lateinit var driverRepo: DriverRepo
    @Autowired private lateinit var orderService: EUOrderService
    @Autowired private lateinit var wsMessageService: WSMessageService

    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val driverFindingJobList = ConcurrentHashMap<Long, Job>()

    fun findOrderForOrder(id: Long) {

        val job = coroutineScope.launch {
            val order = getOrder(id)
            val orderInfo = orderService.getOrderInfo(order)
            sendFindingDriver(order)

            val merchantAddress  = order.merchant?.address ?: return@launch
            val listSuitableDriver = getListDriverNearestToMerchant(merchantAddress)

            for(driver in listSuitableDriver) {
                sendNewOrderRequestToDriver(driver, orderInfo)
                delay(60 * 1000)
            }

            sendNotSuccessFindingDriver(order)
        }

        driverFindingJobList[id] = job
    }

    fun acceptOrderDeliveryRequest(orderId: Long, driverId: Long) {
        driverFindingJobList[orderId]?.cancel()
        driverFindingJobList.remove(orderId)

        val order = orderRecordRepo.findById(orderId).get()
        val driver = driverRepo.findById(driverId).get()

        order.driver = driver
        orderRecordRepo.save(order)

        sendSuccessFindingDriver(order)
    }

    private suspend fun getOrder(orderId: Long): OrderRecord {
        return withContext(Dispatchers.IO) {
            orderRecordRepo.findById(orderId)
        }.get()
    }

    private suspend fun getListDriverNearestToMerchant(address: Address): List<Driver> {
        return withContext(Dispatchers.IO) {
            driverRepo.findNearestDriverToMerchant(address.lat, address.lng)
        }
    }

    fun sendNewOrderRequestToDriver(driver: Driver, orderInfo: OrderInfo) {
        val message = WSMessageWrapper(WS_DRIVER_ORDER_DELIVERY_REQUEST,driver.account.username, WSMessageCode.NEW_ORDER, orderInfo)
        wsMessageService.sendMessage(message)
    }

    private fun sendNotSuccessFindingDriver(order: OrderRecord) {

    }

    private fun sendSuccessFindingDriver(order: OrderRecord) {

    }

    private fun sendFindingDriver(order: OrderRecord) {

    }
}