package com.sonnt.fp_be.model.entities.product

import com.sonnt.fp_be.model.entities.Merchant
import javax.persistence.*

@Entity
class ProductTag(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    var name: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "merchant_id",
        referencedColumnName = "id"
    )
    var merchant: Merchant = Merchant()
) {
}