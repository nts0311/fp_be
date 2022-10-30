package com.sonnt.fp_be.model.entities

import com.sonnt.fp_be.model.entities.stats.MerchantStat
import javax.persistence.*

@Entity
class Merchant(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
    @JoinColumn(name = "account_id")
    var account: Account = Account(),

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
    @JoinColumn(name = "address_id")
    var address: Address? = null,

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "merchant", cascade = [CascadeType.ALL])
    val stat: MerchantStat? = null,

    val subTitle: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val name: String? = null
) {
}