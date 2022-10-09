package com.sonnt.fp_be.features.driver.status

import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import com.sonnt.fp_be.features.shared.services.BaseService
import com.sonnt.fp_be.features.shared.services.FindDriverService
import com.sonnt.fp_be.model.entities.Address
import com.sonnt.fp_be.repos.DriverRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DriverStatusService: BaseService() {
    @Autowired lateinit var driverRepo: DriverRepo

    fun updateLastLocation(address: FPAddressDTO) {
        val driverId = driverRepo.findDriverIdByUserId(userId)
        val driver = driverRepo.findById(driverId).get()

        val newLocation = address.toDbModel()

        if (driver.lastLocation != null) {
            newLocation.id = driver.lastLocation!!.id
        }

        driver.lastLocation = newLocation
        driverRepo.save(driver)
        driverRepo.flush()
    }


}