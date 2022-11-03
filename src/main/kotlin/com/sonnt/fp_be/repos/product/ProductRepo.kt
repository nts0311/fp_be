package com.sonnt.fp_be.repos.product

import com.sonnt.fp_be.model.entities.product.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ProductRepo: JpaRepository<Product, Long> {
    fun findProductByMerchantId(id: Long?, pageable: Pageable): List<Product>
    @Modifying
    @Query("UPDATE Product p SET p.tag = NULL WHERE p.tag.id = :tagId")
    fun clearTag(tagId: Long)

    fun findAllByTagId(menuId: Long): List<Product>
}