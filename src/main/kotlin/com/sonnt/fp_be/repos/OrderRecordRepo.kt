package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.order.OrderRecord
import com.sonnt.fp_be.model.entities.order.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface OrderRecordRepo: JpaRepository<OrderRecord, Long> {
    @Query(value = "SELECT * FROM orders WHERE customer_id=:userId AND status<>'SUCCEED' AND status<>'CANCELED' LIMIT 1",
    nativeQuery = true)
    fun findActiveOrderOf(userId: Long): OrderRecord?

    @Query(value = "SELECT * FROM orders WHERE driver_id=:driverId AND status<>'SUCCEED' AND status<>'CANCELED' LIMIT 1",
        nativeQuery = true)
    fun findActiveOrderOfDriver(driverId: Long): OrderRecord?

    @Query(value = "SELECT * FROM orders WHERE driver_id=:driverId AND status='DELIVERING' LIMIT 1",
        nativeQuery = true)
    fun findDeliveringOrderOfDriver(driverId: Long): OrderRecord?
    @Query(value = "SELECT * FROM orders WHERE merchant_id=:merchantId AND status<>'SUCCEED' AND status<>'CANCELED'",
        nativeQuery = true)
    fun findActiveOrdersOfMerchant(merchantId: Long): List<OrderRecord>

    @Query(value = "SELECT * FROM orders WHERE status='SUCCEED' AND create_date like CONCAT(:date, '%')",
    nativeQuery = true)
    fun findDoneOrderInDay(date: String): List<OrderRecord>

    fun findOrderRecordByCreateDateBetweenAndStatusOrderByCreateDate(fromDateTime: LocalDateTime, toDateTime: LocalDateTime, orderStatus: OrderStatus): List<OrderRecord>

    fun findOrderRecordByMerchantIdAndCreateDateBetweenAndStatusOrderByCreateDate(merchantId: Long, fromDateTime: LocalDateTime, toDateTime: LocalDateTime, orderStatus: OrderStatus): List<OrderRecord>
}