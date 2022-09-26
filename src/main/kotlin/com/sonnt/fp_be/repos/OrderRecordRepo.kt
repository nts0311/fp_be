package com.sonnt.fp_be.repos

import com.sonnt.fp_be.model.entities.order.OrderRecord
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRecordRepo: JpaRepository<OrderRecord, Long> {
}