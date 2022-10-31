package com.sonnt.fp_be.model.entities

import javax.persistence.*

@Entity
class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL])
    @JoinColumn(name = "account_id")
    var account: Account,

    @OneToOne(fetch = FetchType.LAZY,  cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    @JoinColumn(name = "current_address_id")
    var currentAddress: Address? = null,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var address: MutableList<Address> = mutableListOf(),
) {
}