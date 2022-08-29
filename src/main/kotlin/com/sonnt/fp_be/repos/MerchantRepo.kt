package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.Merchant
import org.springframework.data.jpa.repository.JpaRepository

interface MerchantRepo: JpaRepository<Merchant, Long> {
}