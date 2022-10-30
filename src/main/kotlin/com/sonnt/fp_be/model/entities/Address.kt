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
    var ward: String? = null,
    var district: String? = null,
    var city: String? = null,
    var detail: String? = null,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var name: String? = null
) {
}