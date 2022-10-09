package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.Driver
import com.sonnt.fp_be.model.entities.Merchant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DriverRepo: JpaRepository<Driver, Long> {
    @Query(
        value = "SELECT * FROM driver " +
                "         INNER JOIN last_location ll ON driver.id = ll.driver_id " +
                "WHERE (6371 * acos(cos(radians(:merchantLat)) * cos(radians(ll.lat)) * cos(radians(ll.lon) - radians(:merchantLong)) +" +
                "                      sin(radians(:merchantLat)) * sin(radians(ll.lat)))) < 2000 " +
                "ORDER BY (6371 * acos(cos(radians(:merchantLat)) * cos(radians(ll.lat)) * cos(radians(ll.lon) - radians(:merchantLong)) +" +
                "                      sin(radians(:merchantLat)) * sin(radians(ll.lat)))) " +
                "LIMIT 10"
        , nativeQuery = true
    )
    fun findNearestDriverToMerchant(merchantLat: Double, merchantLong: Double): List<Driver>

    @Query(
        value = "SELECT id FROM driver WHERE account_id=:userId LIMIT 1",
        nativeQuery = true
    )
    fun findDriverIdByUserId(userId: Long): Long
}