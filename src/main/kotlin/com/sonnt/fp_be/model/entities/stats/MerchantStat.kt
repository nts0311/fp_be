package com.sonnt.fp_be.model.entities.stats

import com.sonnt.fp_be.model.entities.Merchant
import javax.persistence.*

@Entity
class MerchantStat(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    var numStar: Double = 0.0,
    var numOrder: Long = 0,
    var viewCount: Long = 0
)