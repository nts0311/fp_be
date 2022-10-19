package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.order.OrderRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderRecordRepo: JpaRepository<OrderRecord, Long> {
    @Query(value = "SELECT * FROM orders WHERE customer_id=:userId AND status<>'SUCCEED' AND status<>'CANCELED' LIMIT 1",
    nativeQuery = true)
    fun findActiveOrderOf(userId: Long): OrderRecord?
}