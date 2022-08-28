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

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var address: List<Address> = listOf(),
) {
}