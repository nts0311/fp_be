package com.sonnt.fp_be.features.driver.status

import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import com.sonnt.fp_be.features.shared.services.BaseService
import com.sonnt.fp_be.features.shared.services.OrderTrackingService
import com.sonnt.fp_be.model.entities.DriverStatus
import com.sonnt.fp_be.repos.DriverRepo
import com.sonnt.fp_be.repos.OrderRecordRepo
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DriverStatusService: BaseService() {
    @Autowired lateinit var driverRepo: DriverRepo
    @Autowired lateinit var orderRecordRepo: OrderRecordRepo
    @Autowired lateinit var orderTrackingService: OrderTrackingService

    fun updateLastLocation(address: FPAddressDTO) {
        val driverId = driverRepo.findDriverIdByUserId(userId)
        val driver = driverRepo.findById(driverId).get()

        val newLocation = address.toDbModel()

        if (driver.lastLocation != null) {
            newLocation.id = driver.lastLocation!!.id
        }

        driver.lastLocation = newLocation
        driver.lastUpdateTime = LocalDateTime.now()

        if(driver.status == DriverStatus.INACTIVE) {
            driver.status = DriverStatus.IDLE
        }

        driverRepo.save(driver)
        driverRepo.flush()

        val deliveringOrder = orderRecordRepo.findDeliveringOrderOfDriver(driverId) ?: return

        orderTrackingService.sendDriverLocation(deliveringOrder)

        checkDriverInActive(driverId)
    }

    private fun checkDriverInActive(driverId: Long) {
        GlobalScope.launch {
            delay(60000)
            val driver = driverRepo.findById(driverId).get()
            val lastUpdateTime = driver.lastUpdateTime ?: return@launch
            if (LocalDateTime.now().minusSeconds(60).isAfter(lastUpdateTime) && driver.status == DriverStatus.IDLE) {
                driver.status = DriverStatus.INACTIVE
                withContext(Dispatchers.IO) {
                    driverRepo.save(driver)
                    driverRepo.flush()
                }
            }
        }
    }

}