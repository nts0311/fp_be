package com.sonnt.fp_be.features.shared.dto

import com.sonnt.fp_be.model.entities.Address
import com.sonnt.fp_be.utils.sharedModelMapper

class FPAddressDTO(
    var id: Long = 0,

    var ward: String?,
    var district: String?,
    var city: String?,
    var detail: String?,
    var lat: Double?,
    var long: Double?
) {
    fun toDbModel() = sharedModelMapper.map(this, Address::class.java)
}