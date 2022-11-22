package com.sonnt.fp_be.features.merchant.stats.dto

import com.sonnt.fp_be.model.dto.response.BaseResponse

data class OrderStatsResponse(
    val orderNum: Int,
    val numOfOrderByDay: List<NumOfOrderByDayStat>,
    val averageNumOfOrderByHour: List<AverageNumOfOrderByHourStat>
): BaseResponse()

data class NumOfOrderByDayStat(
    val date: String,
    val numOfOrder: Int
)

data class AverageNumOfOrderByHourStat(
    val date: String,
    val averageNumOfOrders: Double
)