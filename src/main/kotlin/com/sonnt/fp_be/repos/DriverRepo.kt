package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.Driver
import com.sonnt.fp_be.model.entities.Merchant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DriverRepo: JpaRepository<Driver, Long> {
    @Query(
        value = "SELECT * FROM driver " +
                "         INNER JOIN address adr ON driver.last_location_id = adr.id " +
                "WHERE (6371 * acos(cos(radians(:merchantLat)) * cos(radians(adr.lat)) * cos(radians(adr.lng) - radians(:merchantLong)) +" +
                "                      sin(radians(:merchantLat)) * sin(radians(adr.lat)))) < 2000 " +
                "AND driver.status LIKE 'IDLE'" +
                "ORDER BY (6371 * acos(cos(radians(:merchantLat)) * cos(radians(adr.lat)) * cos(radians(adr.lng) - radians(:merchantLong)) +" +
                "                      sin(radians(:merchantLat)) * sin(radians(adr.lat)))) " +
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