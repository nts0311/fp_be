package com.sonnt.fp_be.features.shared.model

import com.sonnt.fp_be.model.entities.Driver

data class DriverDTO(
    val name: String,
    val phone: String,
    val driverPlate: String
)

fun Driver.toDTO() : DriverDTO = DriverDTO(account.name,account.phone, plate ?: "")