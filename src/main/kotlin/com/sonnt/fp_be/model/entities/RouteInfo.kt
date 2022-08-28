package com.sonnt.fp_be.model.entities

import com.sonnt.fp_be.model.entities.order.OrderRecord
import javax.persistence.*


@Entity
class RouteInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "routeInfo")
    var address: List<RouteAddress> = listOf()
) {
}