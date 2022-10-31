package com.sonnt.fp_be.features.merchant.order

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.enduser.EndUserBaseService
import com.sonnt.fp_be.features.enduser.order.model.*
import com.sonnt.fp_be.features.enduser.order.request.CreateOrderRequest
import com.sonnt.fp_be.features.enduser.order.request.GetOrderCheckinInfoRequest
import com.sonnt.fp_be.features.enduser.order.response.GetOrderCheckinInfoResponse
import com.sonnt.fp_be.features.enduser.order.response.OrderInfo
import com.sonnt.fp_be.features.merchant.BaseMerchantService
import com.sonnt.fp_be.features.shared.services.FindDriverService
import com.sonnt.fp_be.features.shared.services.OrderInfoService
import com.sonnt.fp_be.features.shared.services.OrderService
import com.sonnt.fp_be.features.shared.services.OrderTrackingService
import com.sonnt.fp_be.model.entities.DriverStatus
import com.sonnt.fp_be.model.entities.order.*
import com.sonnt.fp_be.repos.AddressRepo
import com.sonnt.fp_be.repos.DriverRepo
import com.sonnt.fp_be.repos.OrderRecordRepo
import com.sonnt.fp_be.repos.product.ProductRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MerchantOrderService: BaseMerchantService() {

    @Autowired lateinit var orderRepo: OrderRecordRepo
    @Autowired lateinit var orderInfoService: OrderInfoService
    @Autowired lateinit var orderTrackingService: OrderTrackingService
    @Autowired lateinit var orderService: OrderService
    @Autowired lateinit var driverRepo: DriverRepo

    fun getActiveOrders(): List<OrderInfo> {
        val activeOrders = orderRepo.findActiveOrdersOfMerchant(currentMerchantId)
            .sortedByDescending { it.createDate }

        return  activeOrders.map { orderInfoService.getOrderInfo(it) }
    }

    fun cancelOrder(orderId: Long) {
        val order = orderRepo.findById(orderId).get()

        if (order.status == OrderStatus.CANCELED) throw BusinessException(FPResponseStatus.badRequest)

        orderService.cancelOrder(order)
        order.driver?.also {
            it.status = DriverStatus.IDLE
            driverRepo.save(it)
            driverRepo.flush()
        }

        orderTrackingService.merchantCanceledOrder(order)
    }


}
