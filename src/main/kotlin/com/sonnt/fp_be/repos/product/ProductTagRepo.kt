package com.sonnt.fp_be.repos.product

import com.sonnt.fp_be.model.entities.product.Product
import com.sonnt.fp_be.model.entities.product.ProductTag
import org.springframework.data.jpa.repository.JpaRepository

interface ProductTagRepo: JpaRepository<ProductTag, Long> {
}