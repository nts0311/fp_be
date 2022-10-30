package com.sonnt.fp_be.features.shared.dto

import com.sonnt.fp_be.model.entities.Address
import com.sonnt.fp_be.utils.sharedModelMapper

class FPAddressDTO(
    var id: Long = 0,
    var ward: String? = null,
    var district: String? = null,
    var city: String? = null,
    var detail: String? = null,
    var lat: Double? = null,
    var lng: Double? = null,
    var name: String? = null
) {
    fun toDbModel() = sharedModelMapper.map(this, Address::class.java)
}