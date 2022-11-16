package com.sonnt.fp_be.model.entities

import javax.persistence.*

@Entity
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    var username: String = "",
    var password: String = "",
    var email: String = "",
    var name: String = "",
    var fcmToken: String = "",
    var role: String = "",
    var phone: String = "",
    var avatarUrl: String? = null,
) {
}