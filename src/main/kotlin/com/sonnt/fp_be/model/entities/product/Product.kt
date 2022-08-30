package com.sonnt.fp_be.model.entities.product

import com.sonnt.fp_be.model.entities.Merchant
import javax.persistence.*

enum class ProductStatus(val value: String) {
    AVAILABLE("AVAILABLE"), OUT_OF_STOCK("OUT_OF_STOCK")
}

@Entity
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    var name: String = "",
    var description: String = "",
    var imageUrl: String = "",
    var price: Double = 0.0,
    @Enumerated(EnumType.STRING)
    var status: ProductStatus = ProductStatus.AVAILABLE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "merchant_id",
        referencedColumnName = "id"
    )
    var merchant: Merchant = Merchant(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "product_id")
    var attributes: MutableList<ProductAttribute> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "category_id",
        referencedColumnName = "id"
    )
    var category: ProductCategory = ProductCategory(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "tag_id",
        referencedColumnName = "id"
    )
    var tag: ProductTag? = null
) {
}