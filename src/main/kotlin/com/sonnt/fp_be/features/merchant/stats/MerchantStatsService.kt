package com.sonnt.fp_be.features.merchant.stats

import com.sonnt.fp_be.features.merchant.BaseMerchantService
import com.sonnt.fp_be.features.merchant.stats.dto.*
import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.features.shared.dto.ProductDTO
import com.sonnt.fp_be.features.shared.services.OrderInfoService
import com.sonnt.fp_be.model.entities.extension.toDTO
import com.sonnt.fp_be.model.entities.extension.toUserProductSelection
import com.sonnt.fp_be.model.entities.order.OrderRecord
import com.sonnt.fp_be.model.entities.order.OrderStatus
import com.sonnt.fp_be.repos.OrderRecordRepo
import com.sonnt.fp_be.repos.product.ProductMenuRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@Service
class MerchantStatsService: BaseMerchantService() {
    @Autowired lateinit var orderRecordRepo: OrderRecordRepo
    @Autowired lateinit var orderInfoService: OrderInfoService
    @Autowired lateinit var productMenuRepo: ProductMenuRepo

    fun getRevenueStats(request: RevenueStatsRequest): RevenueStatsResponse {
        val fromDate = LocalDate.parse(request.fromDate).atStartOfDay()
        val toDate = LocalDate.parse(request.toDate).atEndOfDay()

        val listOrder = orderRecordRepo.findOrderRecordByCreateDateBetweenAndStatusOrderByCreateDate(fromDate, toDate, OrderStatus.SUCCEED)

        val dayRevenues = getRevenueByDay(listOrder)
        val categoriesRevenues = getRevenueByCategory(listOrder)
        val menuRevenues = getRevenueByMenu(listOrder)
        val productRevenueStat = getProductRevenueStat(listOrder)

        print(productRevenueStat.size)

        return RevenueStatsResponse(dayRevenues, categoriesRevenues, menuRevenues, null, null)
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

    fun getRevenueByMenu(listOrder: List<OrderRecord>): List<MenuRevenueStat> {
        val menus = productMenuRepo.findTagByMerchantId(currentMerchantId, Pageable.ofSize(100))
        val priceByMenuId = mutableMapOf<Long, Double>()

        listOrder.forEach { order ->
            val priceByProductId = orderInfoService.calculateProductsPrice(order.toUserProductSelection()).second
            val listProduct = order.items.map { it.product }

            listProduct.forEach {product ->
                val menuId = product.tag!!.id
                priceByMenuId[menuId] = (priceByMenuId[menuId] ?: 0.0) + (priceByProductId[product.id] ?: 0.0)
            }
        }

        val total = priceByMenuId.entries.fold(0.0) {acc, entry -> acc + entry.value }

        return priceByMenuId.entries.map {entry ->
            val menuDTO = menus.first { it.id == entry.key }
            val percentage = entry.value / total * 100
            MenuRevenueStat(menuDTO, percentage)
        }
    }

    fun getProductRevenueStat(listOrder: List<OrderRecord>): List<ProductRevenueStat> {

        val revenueByProductId = mutableMapOf<Long, Double>()
        val countByProductId = mutableMapOf<Long, Int>()

        val products = mutableSetOf<ProductDTO>()

        listOrder.forEach { order ->
            val orderPriceByProductId = orderInfoService.calculateProductsPrice(order.toUserProductSelection()).second
            val listProduct = order.items.map { it.product }

            listProduct.forEach {product ->
                val totalProductRevenue = (revenueByProductId[product.id] ?: 0.0) + (orderPriceByProductId[product.id] ?: 0.0)
                revenueByProductId[product.id] = totalProductRevenue
                countByProductId[product.id] = (countByProductId[product.id] ?: 0) + 1

                products.add(product.toDTO())
            }
        }

        val result = revenueByProductId.entries.map {entry ->
            ProductRevenueStat(
                product = products.first { it.id == entry.key },
                revenue = revenueByProductId[entry.key] ?: 0.0,
                count = countByProductId[entry.key] ?: 0
            )
        }

        return result.sortedWith(Comparator { s1, s2 ->
            return@Comparator if(s1.revenue > s2.revenue) -1
            else if (s1.revenue < s2.revenue) 1
            else {
                if (s1.count > s2.count) 1
                else if (s1.count < s2.count) -1
                else 0
            }
        })
    }


}

fun LocalDate.atEndOfDay(): LocalDateTime = LocalDateTime.of(this, LocalTime.MAX)