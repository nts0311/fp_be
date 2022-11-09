package com.sonnt.fp_be.features.merchant.info

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.enduser.EndUserBaseService
import com.sonnt.fp_be.features.enduser.order.model.*
import com.sonnt.fp_be.features.enduser.order.request.CreateOrderRequest
import com.sonnt.fp_be.features.enduser.order.request.GetOrderCheckinInfoRequest
import com.sonnt.fp_be.features.enduser.order.response.GetOrderCheckinInfoResponse
import com.sonnt.fp_be.features.enduser.order.response.OrderInfo
import com.sonnt.fp_be.features.merchant.BaseMerchantService
import com.sonnt.fp_be.features.merchant.info.dto.ChangeMerchantActiveHourRequest
import com.sonnt.fp_be.features.merchant.info.dto.ChangeMerchantActivityStatusRequest
import com.sonnt.fp_be.features.merchant.info.dto.GetMerchantInfoResponse
import com.sonnt.fp_be.features.shared.services.FindDriverService
import com.sonnt.fp_be.features.shared.services.OrderInfoService
import com.sonnt.fp_be.features.shared.services.OrderService
import com.sonnt.fp_be.features.shared.services.OrderTrackingService
import com.sonnt.fp_be.model.entities.DriverStatus
import com.sonnt.fp_be.model.entities.order.*
import com.sonnt.fp_be.repos.AddressRepo
import com.sonnt.fp_be.repos.DriverRepo
import com.sonnt.fp_be.repos.MerchantRepo
import com.sonnt.fp_be.repos.OrderRecordRepo
import com.sonnt.fp_be.repos.product.ProductRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.Date

@Service
class MerchantInfoService: BaseMerchantService() {
    fun changeActivityRequest(request: ChangeMerchantActivityStatusRequest) {
        val merchant = merchantRepo.findById(currentMerchantId).get()
        request.open?.also {
            merchant.isOpening = it
            merchantRepo.save(merchant)
            merchantRepo.flush()
        }
    }

    fun changeActiveHour(request: ChangeMerchantActiveHourRequest) {
        val merchant = merchantRepo.findById(currentMerchantId).get()

        request.openingHour?.also {
            val openingHour = LocalTime.parse(request.openingHour)
            merchant.openingHour = openingHour
        }

        request.closingHour?.also {
            val closingHour = LocalTime.parse(request.closingHour)
            merchant.closingHour = closingHour
        }

        if (request.openingHour != null || request.closingHour != null) {
            merchantRepo.save(merchant)
            merchantRepo.flush()
        }
    }

    fun getMerchantInfo(): GetMerchantInfoResponse {
        val merchant = merchantRepo.findById(currentMerchantId).get()
        return GetMerchantInfoResponse(
            isOpening = merchant.isOpening,
            openingHour = merchant.openingHour.toString(),
            closingHour = merchant.closingHour.toString()
        )
    }
}
