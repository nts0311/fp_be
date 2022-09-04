package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.Address
import org.springframework.data.jpa.repository.JpaRepository

interface AddressRepo: JpaRepository<Address, Long> {
}