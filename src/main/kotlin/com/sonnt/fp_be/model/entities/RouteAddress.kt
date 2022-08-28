package com.sonnt.fp_be.model.entities

import javax.persistence.*

@Entity
class RouteAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "route_info_id",
        referencedColumnName = "id"
    )
    var routeInfo: RouteInfo = RouteInfo(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "address_id",
        referencedColumnName = "id"
    )
    var address: Address = Address()
) {
}