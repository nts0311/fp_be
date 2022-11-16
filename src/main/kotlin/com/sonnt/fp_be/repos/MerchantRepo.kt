package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.Merchant
import com.sonnt.fp_be.model.entities.product.ProductCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MerchantRepo : JpaRepository<Merchant, Long> {
    fun findMerchantByAccountId(accountId: Long): Merchant

    @Query(
        value = "SELECT * FROM merchant " +
                "         INNER JOIN address a ON merchant.address_id = a.id " +
                "WHERE merchant.is_opening=true AND (current_time() between merchant.opening_hour and merchant.closing_hour) " +
                "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(a.lat)) * cos(radians(a.lng) - radians(:long)) +" +
                "                      sin(radians(:lat)) * sin(radians(a.lat)))) " +
                "LIMIT :pageSize OFFSET :page"
        , nativeQuery = true
    )
    fun findNearestMerchantByCord(lat: Double, long: Double, pageSize: Int = 10, page: Int = 0): List<Merchant>

    @Query(
        value = "SELECT DISTINCT merchant.* FROM merchant " +
                "inner join product p on merchant.id = p.merchant_id " +
                "inner join product_category pc on p.category_id = pc.id " +
                "WHERE p.category_id=:categoryId and merchant.name like CONCAT('%', :searchKey, '%') " +
                "AND merchant.is_opening=true AND (current_time() between merchant.opening_hour and merchant.closing_hour)"

        , nativeQuery = true
    )
    fun findMerchantsWithCategory(categoryId: Long, searchKey: String, pageable: Pageable): List<Merchant>

    @Query(
        value = "SELECT * FROM merchant " +
                "WHERE merchant.name like CONCAT('%', :name, '%') " +
                "AND merchant.is_opening=true AND (current_time() between merchant.opening_hour and merchant.closing_hour)"

        , nativeQuery = true
    )
    fun findMerchantsWithName(name: String, pageable: Pageable): List<Merchant>

    fun findMerchantByName(name: String): Merchant

    @Query(
        value = "SELECT DISTINCT product_category.* FROM product_category " +
                "inner join product p on product_category.id = p.category_id " +
                "inner join merchant m on p.merchant_id = m.id " +
                "WHERE m.id=:merchantId",

        nativeQuery = true
    )
    fun findCategoriesOfMerchant(merchantId: Long): List<ProductCategory>

}