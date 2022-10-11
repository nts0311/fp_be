package com.sonnt.fp_be.features.shared.services

import com.sonnt.fp_be.features.enduser.order.EUOrderService
import com.sonnt.fp_be.features.enduser.order.response.OrderInfo
import com.sonnt.fp_be.features.shared.model.WSMessageCode
import com.sonnt.fp_be.features.shared.model.WSMessageWrapper
import com.sonnt.fp_be.model.entities.Address
import com.sonnt.fp_be.model.entities.Driver
import com.sonnt.fp_be.model.entities.DriverStatus
import com.sonnt.fp_be.model.entities.order.OrderRecord
import com.sonnt.fp_be.repos.DriverRepo
import com.sonnt.fp_be.repos.OrderRecordRepo
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

//🥺
@Service
class FindDriverService {

    @Autowired private lateinit var orderRecordRepo: OrderRecordRepo
    @Autowired private lateinit var driverRepo: DriverRepo
    @Autowired private lateinit var orderService: EUOrderService
    @Autowired private lateinit var orderTrackingService: OrderTrackingService

    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val driverFindingJobList = ConcurrentHashMap<Long, Job>()
    val suitableDriverList = ConcurrentHashMap<Long, List<Driver>>()

    fun findOrderForOrder(id: Long) {

        val job = coroutineScope.launch {
            val order = getOrder(id)
            val orderInfo = orderService.getOrderInfo(order)

            val merchantAddress  = order.merchant?.address ?: return@launch
            val listSuitableDriver = getListDriverNearestToMerchant(merchantAddress)
            suitableDriverList[id] = listSuitableDriver

            withContext(Dispatchers.IO) {
                updateDriverStatus(listSuitableDriver, DriverStatus.QUEUE_FOR_NEW_ORDER)
            }

            for(driver in listSuitableDriver) {
                orderTrackingService.sendNewOrderRequestToDriver(driver, orderInfo)
                delay(60 * 1000)
            }

            orderTrackingService.sendNotSuccessFindingDriver(order)
        }

        job.invokeOnCompletion {
            val listDriverQueueing = suitableDriverList[id] ?: return@invokeOnCompletion
            updateDriverStatus(listDriverQueueing, DriverStatus.IDLE)
        }

        driverFindingJobList[id] = job
    }

    fun acceptOrderDeliveryRequest(orderId: Long, driverId: Long) {
        driverFindingJobList[orderId]?.cancel()
        driverFindingJobList.remove(orderId)

        val listDriverQueueing = suitableDriverList[orderId] ?: return
        updateDriverStatus(listDriverQueueing, DriverStatus.IDLE)

        suitableDriverList.remove(orderId)
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

    private fun updateDriverStatus(drivers: List<Driver>, status: DriverStatus) {
            drivers.forEach {
                it.status = status
            }

            driverRepo.saveAll(drivers)
            driverRepo.flush()
    }

}