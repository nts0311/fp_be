package com.sonnt.fp_be.model.entities.stats

import com.sonnt.fp_be.model.entities.Driver
import javax.persistence.*

@Entity
class DriverStat(
    @Id
    var id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    var driver: Driver,

    var numStar: Double
    )