package com.sonnt.fp_be.features.merchant.stats

import com.sonnt.fp_be.features.merchant.BaseMerchantService
import com.sonnt.fp_be.features.merchant.stats.dto.CategoriesRevenueStat
import com.sonnt.fp_be.features.merchant.stats.dto.DayRevenueStat
import com.sonnt.fp_be.features.merchant.stats.dto.RevenueStatsRequest
import com.sonnt.fp_be.features.merchant.stats.dto.RevenueStatsResponse
import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.features.shared.services.OrderInfoService
import com.sonnt.fp_be.model.entities.extension.toDTO
import com.sonnt.fp_be.model.entities.extension.toUserProductSelection
import com.sonnt.fp_be.model.entities.order.OrderRecord
import com.sonnt.fp_be.model.entities.order.OrderStatus
import com.sonnt.fp_be.repos.OrderRecordRepo
import com.sonnt.fp_be.repos.product.ProductRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Service
class MerchantStatsService: BaseMerchantService() {
    @Autowired lateinit var orderRecordRepo: OrderRecordRepo
    @Autowired lateinit var orderInfoService: OrderInfoService
    @Autowired lateinit var productRepo: ProductRepo

    fun getRevenueStats(request: RevenueStatsRequest): RevenueStatsResponse {
        val fromDate = LocalDate.parse(request.fromDate).atStartOfDay()
        val toDate = LocalDate.parse(request.toDate).atEndOfDay()

        val listOrder = orderRecordRepo.findOrderRecordByCreateDateBetweenAndStatusOrderByCreateDate(fromDate, toDate, OrderStatus.SUCCEED)

        val dayRevenues = getRevenueByDay(listOrder)
        val categoriesRevenues = getRevenueByCategory(listOrder)

        println(dayRevenues.size)
        println(categoriesRevenues.size)

        return RevenueStatsResponse(listOf(), listOf(), listOf(), null, null)
    }

    fun getRevenueByDay(listOrder: List<OrderRecord>): List<DayRevenueStat> {
        return listOrder.groupBy { it.createDate.toLocalDate().toString() }.entries.map {entry ->
            val revenue = entry.value.fold(0.0) {acc, orderRecord ->
                val orderInfo = orderInfoService.getOrderInfo(orderRecord)
                acc + orderInfo.paymentInfo.price
            }
            DayRevenueStat(entry.key, revenue)
        }
    }

    fun getRevenueByCategory(listOrder: List<OrderRecord>): List<CategoriesRevenueStat> {
        val categories = mutableSetOf<ProductCategoryDTO>()

        val priceByCategory = mutableMapOf<Long, Double>()

        listOrder.forEach { order ->
            val priceByProductId = orderInfoService.calculateProductsPrice(order.toUserProductSelection()).second
            val listProduct = order.items.map { it.product }

            listProduct.forEach {product ->
                categories.add(product.category.toDTO())
                val categoryId = product.category.id
                priceByCategory[categoryId] = (priceByCategory[categoryId] ?: 0.0) + (priceByProductId[product.id] ?: 0.0)
            }
        }

        val total = priceByCategory.entries.fold(0.0) {acc, entry -> acc + entry.value }

        return priceByCategory.entries.map {entry ->
            val categoryDTO = categories.first { it.id == entry.key }
            val percentage = entry.value / total * 100
            CategoriesRevenueStat(categoryDTO, percentage)
        }
    }

}

fun LocalDate.atEndOfDay(): LocalDateTime = LocalDateTime.of(this, LocalTime.MAX)