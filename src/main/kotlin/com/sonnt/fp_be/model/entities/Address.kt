package com.sonnt.fp_be.model.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    var ward: String?,
    var district: String?,
    var city: String?,
    var detail: String?,
    var lat: Double = 0.0,
    var long: Double = 0.0
) {
}