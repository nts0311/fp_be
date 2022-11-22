package com.sonnt.fp_be.features.merchant.stats

import com.sonnt.fp_be.features.merchant.info.dto.ChangeMerchantActiveHourRequest
import com.sonnt.fp_be.features.merchant.info.dto.ChangeMerchantActivityStatusRequest
import com.sonnt.fp_be.features.merchant.order.dto.GetMerchantActiveOrdersResponse
import com.sonnt.fp_be.features.merchant.order.dto.GetMerchantDoneOrdersResponse
import com.sonnt.fp_be.features.merchant.stats.dto.DayRevenueStat
import com.sonnt.fp_be.features.merchant.stats.dto.RevenueStatsRequest
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/merchant/stats")
class MerchantStatsController: BaseController() {

    @Autowired
    lateinit var statsService: MerchantStatsService

    @GetMapping("revenue")
    fun getRevenueStat(
        @RequestParam fromDate: String,
        @RequestParam toDate: String
    ): ResponseEntity<*> {
        val response = statsService.getRevenueStats(RevenueStatsRequest(fromDate, toDate))
        return ok(response)
    }

    @GetMapping("order")
    fun getOrderStat(
        @RequestParam fromDate: String,
        @RequestParam toDate: String
    ): ResponseEntity<*> {
        val response = statsService.getOrderStats(RevenueStatsRequest(fromDate, toDate))
        return ok(response)
    }

    @GetMapping("product")
    fun getProductStat(
        @RequestParam fromDate: String,
        @RequestParam toDate: String
    ): ResponseEntity<*> {
        val response = statsService.getProductStats(RevenueStatsRequest(fromDate, toDate))
        return ok(response)
    }

}
