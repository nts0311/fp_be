package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.AppUser
import org.springframework.data.jpa.repository.JpaRepository

interface AppUserRepo: JpaRepository<AppUser, Long> {
    fun findByUsername(username: String): AppUser?
}