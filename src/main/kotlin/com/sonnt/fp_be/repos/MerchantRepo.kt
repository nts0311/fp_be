package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.Merchant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MerchantRepo: JpaRepository<Merchant, Long> {
    fun findMerchantByAccountId(accountId: Long): Merchant

    @Query(value = "SELECT * " +
            "FROM merchant " +
            "         inner join address a on merchant.address_id = a.id " +
            "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(a.lat)) * cos(radians(a.lon) - radians(:long)) +" +
            "                      sin(radians(:lat)) * sin(radians(a.lat)))) " +
            "LIMIT :pageSize OFFSET :page"
        , nativeQuery = true)
    fun findNearestMerchantByCord(lat: Double, long: Double, pageSize: Int = 10, page: Int = 0): List<Merchant>
}