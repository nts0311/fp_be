package com.sonnt.fp_be.model.entities

import com.sonnt.fp_be.model.entities.stats.DriverStat
import javax.persistence.*

@Entity
class Driver(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
    @JoinColumn(name = "account_id")
    var account: Account,

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "driver", cascade = [CascadeType.ALL])
    val stat: DriverStat? = null,

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "driver", cascade = [CascadeType.ALL])
    val lastLocation: LastLocation? = null
) {
}