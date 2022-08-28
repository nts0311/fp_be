package com.sonnt.fp_be.model.entities

import javax.persistence.*

@Entity
class LastLocation(
    @Id
    var id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    var driver: Driver,

    var lat: Double,
    var lon: Double
)
{

}