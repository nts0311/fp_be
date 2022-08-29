package com.sonnt.fp_be.repos.product

import com.sonnt.fp_be.model.entities.product.ProductAttribute
import org.springframework.data.jpa.repository.JpaRepository

interface ProductAttributeRepo: JpaRepository<ProductAttribute, Long> {
}