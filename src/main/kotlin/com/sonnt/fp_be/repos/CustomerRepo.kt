package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepo: JpaRepository<Customer, Long> {
}