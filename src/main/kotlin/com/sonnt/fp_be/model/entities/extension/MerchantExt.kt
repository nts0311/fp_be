package com.sonnt.fp_be.model.entities.extension

import com.sonnt.fp_be.features.shared.dto.MerchantItemDTO
import com.sonnt.fp_be.model.entities.Merchant
import com.sonnt.fp_be.utils.GPSUtils
import com.sonnt.fp_be.utils.sharedModelMapper
import java.time.LocalTime

fun Merchant.toItemDTO(): MerchantItemDTO = MerchantItemDTO(
        id = id,
        name = account.name,
        subTitle = subTitle,
        numStar =  stat?.numStar,
        numOrder = stat?.numOrder,
        address = address
    )

fun List<Merchant>.toMerchantItemList() = this.map { it.toItemDTO() }

fun Merchant.isOpening(): Boolean {
        val currentTime = LocalTime.now()
        return isOpening && currentTime in (openingHour..closingHour)
}