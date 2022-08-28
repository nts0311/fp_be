package com.sonnt.fp_be.model.entities.stats

import com.sonnt.fp_be.model.entities.Merchant
import javax.persistence.*

@Entity
class MerchantStat(
    @Id
    var id: Long = 0,
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    var merchant: Merchant? = null,
    var numStar: Double = 0.0,
    var numOrder: Long = 0,
    var viewCount: Long = 0
)