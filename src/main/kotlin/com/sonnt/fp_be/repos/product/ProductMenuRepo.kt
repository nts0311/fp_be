package com.sonnt.fp_be.repos.product

import com.sonnt.fp_be.model.entities.product.ProductMenu
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ProductMenuRepo: JpaRepository<ProductMenu, Long> {
    fun findTagByMerchantId(id: Long?, pageable: Pageable): List<ProductMenu>
}