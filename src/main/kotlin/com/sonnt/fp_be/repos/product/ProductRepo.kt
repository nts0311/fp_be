package com.sonnt.fp_be.repos.product

import com.sonnt.fp_be.model.entities.product.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepo: JpaRepository<Product, Long> {
}