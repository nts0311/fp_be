package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.Merchant
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
                "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(a.lat)) * cos(radians(a.lng) - radians(:long)) +" +
                "                      sin(radians(:lat)) * sin(radians(a.lat)))) " +
                "LIMIT :pageSize OFFSET :page"
        , nativeQuery = true
    )
    fun findNearestMerchantByCord(lat: Double, long: Double, pageSize: Int = 10, page: Int = 0): List<Merchant>

    @Query(
        value = "SELECT DISTINCT merchant.* FROM merchant " +
                "inner join product p on merchant.id = p.merchant_id" +
                "inner join product_category pc on p.category_id = pc.id " +
                "WHERE category_id=:categoryId and merchant.name like '%:searchKey%'"
        , nativeQuery = true
    )
    fun findMerchantsWithCategory(categoryId: Long, @Param("searchKey") searchKey: String, pageable: Pageable): Page<List<Merchant>>

    fun findMerchantsByNameLikeIgnoreCase(name: String, pageable: Pageable): Page<List<Merchant>>
}