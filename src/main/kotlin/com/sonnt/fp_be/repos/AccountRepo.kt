package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepo: JpaRepository<Account, Long> {
    fun findByUsername(username: String): Account?
}