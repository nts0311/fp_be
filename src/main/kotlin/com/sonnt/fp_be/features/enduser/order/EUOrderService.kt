package com.sonnt.fp_be.features.enduser.order

import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.enduser.EndUserBaseService
import com.sonnt.fp_be.features.enduser.order.model.*
import com.sonnt.fp_be.features.enduser.order.request.CreateOrderRequest
import com.sonnt.fp_be.features.enduser.order.request.GetOrderCheckinInfoRequest
import com.sonnt.fp_be.features.enduser.order.request.RateOrderRequest
import com.sonnt.fp_be.features.enduser.order.response.GetOrderCheckinInfoResponse
import com.sonnt.fp_be.features.enduser.order.response.OrderInfo
import com.sonnt.fp_be.features.shared.services.FindDriverService
import com.sonnt.fp_be.features.shared.services.OrderInfoService
import com.sonnt.fp_be.model.entities.extension.isOpening
import com.sonnt.fp_be.model.entities.order.*
import com.sonnt.fp_be.model.entities.product.ProductStatus
import com.sonnt.fp_be.repos.AddressRepo
import com.sonnt.fp_be.repos.DriverRepo
import com.sonnt.fp_be.repos.MerchantRepo
import com.sonnt.fp_be.repos.OrderRecordRepo
import com.sonnt.fp_be.repos.product.ProductRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EUOrderService: EndUserBaseService() {

    @Autowired lateinit var productRepo: ProductRepo
    @Autowired lateinit var orderRepo: OrderRecordRepo
    @Autowired lateinit var addressRepo: AddressRepo
    @Autowired lateinit var findDriverService: FindDriverService
    @Autowired lateinit var orderInfoService: OrderInfoService
    @Autowired lateinit var driverRepo: DriverRepo
    @Autowired lateinit var merchantRepo: MerchantRepo

    fun getOrderCheckinInfo(request: GetOrderCheckinInfoRequest): GetOrderCheckinInfoResponse {
        val cartProducts = request.userProductSelection
        if(cartProducts.isEmpty()) throw BusinessException(FPResponseStatus.cartEmpty)

        val merchant = productRepo.findById(cartProducts.first().productId).get().merchant
        val merchantAddress = merchant.address ?: throw BusinessException(FPResponseStatus.notFoung)
        val euAddress = request.euAddress.toDbModel()

        val orderEstimatedRouteInfo = orderInfoService.getOrderEstimateRoute(merchantAddress, euAddress)
        val orderPaymentInfo = orderInfoService.getOrderPaymentInfo(orderEstimatedRouteInfo.getDistanceInMeter(), request.userProductSelection)

        return GetOrderCheckinInfoResponse(orderEstimatedRouteInfo, orderPaymentInfo)
    }

    fun createOrder(request: CreateOrderRequest): Long {
        val userProductSelection = request.userProductSelection
        val customer = customerRepo.findCustomerByAccountId(userId)
        val merchant = productRepo.findById(userProductSelection.first().productId).get().merchant
        val orderItems = userProductSelection.map { userProductSelectionToOrderItem(it) }.toMutableList()

        val merchantAddress = merchant.address ?: throw BusinessException(FPResponseStatus.notFoung)
        val euAddress = addressRepo.findById(request.addressId).get()
        val orderEstimatedRouteInfo = orderInfoService.getOrderEstimateRoute(merchantAddress, euAddress)

        if (!merchant.isOpening()) {
            throw BusinessException(FPResponseStatus.merchantClosed)
        }

        orderItems.forEach {orderItem ->
            if (productRepo.getStatusOfProduct(orderItem.product.id) != ProductStatus.AVAILABLE){
                throw BusinessException(FPResponseStatus.productNotAvailable)
            }
        }

        val newOrder = OrderRecord(
            note = request.note,
            items = orderItems,
            customer = customer,
            merchant = merchant,
            receivingAddress = euAddress,
            status = OrderStatus.SEARCHING_DRIVER,
            estimatedRouteInfo = orderEstimatedRouteInfo
        )

        orderRepo.save(newOrder)
        orderRepo.flush()

        val orderInfo = orderInfoService.getOrderInfo(newOrder)
        findDriverService.findOrderForOrder(orderInfo)

        return newOrder.id
    }

    fun getUserActiveOrder(): OrderInfo? {
        val customerId = customerRepo.findCustomerByAccountId(userId)?.id ?: return null
        val order = orderRepo.findActiveOrderOf(customerId) ?: return null
        return orderInfoService.getOrderInfo(order)
    }

    private fun userProductSelectionToOrderItem(userProductSelection: UserProductSelection): OrderItem {
        val orderItem = OrderItem()
        val product = productRepo.findById(userProductSelection.productId).get()

        val orderAttributes = mutableListOf<OrderItemAttribute>()

        for (attributeSelection in userProductSelection.attributeSelections) {
            val attr = product.attributes.first { it.id == attributeSelection.attributeId }

            val orderAttr = OrderItemAttribute()
            orderAttr.attribute = attr

            orderAttr.options = attr.options
                .filter { attributeSelection.optionsId.contains(it.id) }
                .map { OrderItemAttributeOption(option = it) }
                .toMutableList()

            orderAttributes.add(orderAttr)
        }

        orderItem.attributes = orderAttributes
        orderItem.num = userProductSelection.num
        orderItem.product = product

        return orderItem
    }

    fun getOrderInfo(orderId: Long): OrderInfo? {
        return orderInfoService.getOrderInfo(orderId)
    }

    fun saveRate(body: RateOrderRequest) {
        val order = orderRepo.findById(body.orderId).get()

        val driver = order.driver

        driver?.stat?.also { stat ->

            val totalStar = stat.numStar * stat.numOrder
            stat.numOrder++
            stat.numStar = (totalStar + body.driverStat.toDouble()) / stat.numOrder.toDouble()

            driverRepo.save(driver)
            driverRepo.flush()
        }

        val merchant = order.merchant

        merchant?.stat?.also { stat ->

            val totalStar = stat.numStar * stat.numOrder
            stat.numOrder++
            stat.numStar = (totalStar + body.merchantStar.toDouble()) / stat.numOrder.toDouble()

            merchantRepo.save(merchant)
            merchantRepo.flush()
        }
    }
}

fun Double?.safeDouble() = this ?: 0.0