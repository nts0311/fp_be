package com.sonnt.fp_be.model.entities.extension

import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import com.sonnt.fp_be.model.entities.Address
import com.sonnt.fp_be.utils.sharedModelMapper

fun Address.toDTO(): FPAddressDTO {
    return sharedModelMapper.map(this, FPAddressDTO::class.java)
}