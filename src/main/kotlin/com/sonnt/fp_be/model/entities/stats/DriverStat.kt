package com.sonnt.fp_be.model.entities.stats

import com.sonnt.fp_be.model.entities.Driver
import javax.persistence.*

@Entity
class DriverStat(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    var numStar: Double = 0.0,

    var numOrder: Long = 0
)