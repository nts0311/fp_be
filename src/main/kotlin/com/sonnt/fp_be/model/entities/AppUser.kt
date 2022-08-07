package com.sonnt.fp_be.model.entities

import javax.persistence.*

@Entity
class AppUser(
    @Id
    @SequenceGenerator(
        name = "appuser_sequence",
        sequenceName = "appuser_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "appuser_sequence"
    )
    var id: Long = 0,
    var username: String = "",
    var password: String = "",
    var email: String = "",
    var name: String = "",
    var fcmToken: String = ""
) {
}