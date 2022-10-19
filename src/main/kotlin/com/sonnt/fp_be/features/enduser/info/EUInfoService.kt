package com.sonnt.fp_be.features.enduser.info

import com.sonnt.fp_be.features.enduser.EndUserBaseService
import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import com.sonnt.fp_be.features.shared.model.Cord
import com.sonnt.fp_be.utils.GPSUtils
import org.springframework.stereotype.Service

@Service
class EUInfoService: EndUserBaseService() {
    val DELTA_DISTANCE = 20

    fun getUserLocation(currentLocation: FPAddressDTO): FPAddressDTO {
        val userLocations = customerRepo.findCustomerByAccountId(userId)?.address ?: return currentLocation

        val userCord = Cord(currentLocation.lat ?: 0.0, currentLocation.lng ?:0.0)

        for (address in userLocations) {
            val cord = Cord(address.lat, address.lng)
            if (GPSUtils.distance(userCord, cord) <= DELTA_DISTANCE) {
                return modelMapper.map(address, FPAddressDTO::class.java)
            }
        }

        return currentLocation
    }
}