package com.sonnt.fp_be.features.enduser.info

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.enduser.EndUserBaseService
import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import com.sonnt.fp_be.features.shared.model.Cord
import com.sonnt.fp_be.model.entities.Address
import com.sonnt.fp_be.model.entities.Customer
import com.sonnt.fp_be.model.entities.extension.toDTO
import com.sonnt.fp_be.utils.GPSUtils
import org.springframework.stereotype.Service

@Service
class EUInfoService: EndUserBaseService() {
    val DELTA_DISTANCE = 5

    fun getUserLocation(currentAddress: FPAddressDTO): FPAddressDTO {
        val customer = customerRepo.findCustomerByAccountId(userId) ?: throw BusinessException(FPResponseStatus.notFoung)
        val userLocations = customer.address

        if(userLocations.isEmpty()) {
            val address = modelMapper.map(currentAddress, Address::class.java)
            customer.address = mutableListOf(address)

            customer.currentAddress = address
            customerRepo.save(customer)
            customerRepo.flush()

            return currentAddress
        } else {
            val userCord = Cord(currentAddress.lat ?: 0.0, currentAddress.lng ?:0.0)

            for (address in userLocations) {
                val cord = Cord(address.lat, address.lng)
                if (GPSUtils.distance(userCord, cord) <= DELTA_DISTANCE) {
                    setEndUserCurrentAddress(customer, address)
                    return modelMapper.map(address, FPAddressDTO::class.java)
                }
            }

            val addressId = customer.currentAddress!!.id
            modelMapper.map(currentAddress, customer.currentAddress)
            customer.currentAddress?.id = addressId
            customerRepo.save(customer)
            customerRepo.flush()

            return currentAddress
        }
    }

    private fun setEndUserCurrentAddress(eu: Customer, address: Address) {
        eu.currentAddress = address
        customerRepo.save(eu)
        customerRepo.flush()
    }

    fun getUserLocationList(): List<FPAddressDTO> {
        return customerRepo.findCustomerByAccountId(userId)?.address?.map { it.toDTO() } ?: listOf()
    }

    fun addUserLocation(addressDTO: FPAddressDTO) {
        val user = customerRepo.findCustomerByAccountId(userId) ?: return
        val address = addressDTO.toDbModel()
        user.address.add(address)
        customerRepo.save(user)
        customerRepo.flush()
    }

}