package com.sonnt.fp_be.model.entities.order

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
class OrderEstimatedRouteInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    val durationInSec: Long? = null,
    val distanceInMeter: Long? = null,
    val distanceReadable: String? = null
) {
    fun getDistanceInMeter(): Long {
        return distanceInMeter ?: 0
    }
}