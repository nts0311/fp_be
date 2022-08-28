package com.sonnt.fp_be.model.entities.product

import javax.persistence.*

@Entity
class ProductAttribute(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    var name: String = "",
    var isRequired: Boolean = false,
    var isMultiple: Boolean = false,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "attribute_id")
    var options: List<ProductAttributeOption> = listOf()
) {
}