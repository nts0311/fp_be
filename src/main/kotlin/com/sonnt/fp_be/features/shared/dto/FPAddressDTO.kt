package com.sonnt.fp_be.features.shared.dto

class FPAddressDTO(
    var id: Long = 0,

    var ward: String?,
    var district: String?,
    var city: String?,
    var detail: String?,
    var lat: Double?,
    var long: Double?
) {
}