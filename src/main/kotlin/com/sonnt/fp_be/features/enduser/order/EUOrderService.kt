package com.sonnt.fp_be.features.enduser.order

import com.google.maps.DistanceMatrixApi
import com.google.maps.model.DistanceMatrix
import com.google.maps.model.LatLng
import com.google.maps.model.TrafficModel
import com.google.maps.model.TravelMode
import com.sonnt.fp_be.exceptions.BusinessException
import com.sonnt.fp_be.exceptions.FPResponseStatus
import com.sonnt.fp_be.features.enduser.EndUserBaseService
import com.sonnt.fp_be.features.enduser.order.model.*
import com.sonnt.fp_be.features.enduser.order.request.CreateOrderRequest
import com.sonnt.fp_be.features.enduser.order.request.GetOrderCheckinInfoRequest
import com.sonnt.fp_be.features.enduser.order.response.GetOrderCheckinInfoResponse
import com.sonnt.fp_be.features.enduser.order.response.OrderInfo
import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import com.sonnt.fp_be.features.shared.services.mapApiContext
import com.sonnt.fp_be.model.entities.Address
import com.sonnt.fp_be.model.entities.extension.calculatePrice
import com.sonnt.fp_be.model.entities.extension.toOrderedProductItem
import com.sonnt.fp_be.model.entities.extension.toUserProductSelection
import com.sonnt.fp_be.model.entities.order.*
import com.sonnt.fp_be.repos.AddressRepo
import com.sonnt.fp_be.repos.OrderRecordRepo
import com.sonnt.fp_be.repos.product.ProductRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Date

@Service
class EUOrderService: EndUserBaseService() {

    @Autowired lateinit var productRepo: ProductRepo
    @Autowired lateinit var orderRepo: OrderRecordRepo
    @Autowired lateinit var addressRepo: AddressRepo

    val serviceFee = 2000.0
    val deliveryFeePerKm = 10000

    fun getOrderCheckinInfo(request: GetOrderCheckinInfoRequest): GetOrderCheckinInfoResponse {
        val cartProducts = request.userProductSelection
        if(cartProducts.isEmpty()) throw BusinessException(FPResponseStatus.cartEmpty)

        val merchant = productRepo.findById(cartProducts.first().productId).get().merchant
        val merchantAddress = merchant.address ?: throw BusinessException(FPResponseStatus.notFoung)
        val euAddress = request.euAddress.toDbModel()

        val orderEstimatedRouteInfo = getOrderEstimateRoute(merchantAddress, euAddress)
        val orderPaymentInfo = getOrderPaymentInfo(orderEstimatedRouteInfo.getDistanceInMeter(), request.userProductSelection)

        return GetOrderCheckinInfoResponse(orderEstimatedRouteInfo, orderPaymentInfo)
    }

    fun createOrder(request: CreateOrderRequest): Long {
        val userProductSelection = request.userProductSelection
        val customer = customerRepo.findCustomerByAccountId(userId)
        val merchant = productRepo.findById(userProductSelection.first().productId).get().merchant
        val orderItems = userProductSelection.map { userProductSelectionToOrderItem(it) }.toMutableList()

        val merchantAddress = merchant.address ?: throw BusinessException(FPResponseStatus.notFoung)
        val euAddress = addressRepo.findById(request.addressId).get()
        val orderEstimatedRouteInfo = getOrderEstimateRoute(merchantAddress, euAddress)

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

        return newOrder.id
    }

    fun getOrderInfo(orderId: Long): OrderInfo {
        val order = orderRepo.findById(orderId).get()
        return getOrderInfo(order)
    }

    fun getOrderInfo(order: OrderRecord): OrderInfo {
        val orderedItem = order.items.map {
            it.toOrderedProductItem()
        }

        val fromAddress = order.merchant?.address!!
        val toAddress = order.receivingAddress!!

        val orderEstimatedRouteInfo = order.estimatedRouteInfo!!

        val orderPaymentInfo = getOrderPaymentInfo(orderEstimatedRouteInfo.getDistanceInMeter(), order.toUserProductSelection())

        return OrderInfo(order.id,
            order.status.value,
            order.createDate,
            modelMapper.map(fromAddress, FPAddressDTO::class.java),
            modelMapper.map(toAddress, FPAddressDTO::class.java),
            orderEstimatedRouteInfo,
            orderedItem,
            orderPaymentInfo,
            order.customer?.account?.name ?: "",
            order.customer?.account?.phone ?: "",
            order.merchant?.account?.name ?: "",
            order.merchant?.account?.phone ?: "",
            order.driver?.account?.name,
            order.driver?.account?.phone,
            order.driver?.plate
        )
    }


    private fun  userProductSelectionToOrderItem(userProductSelection: UserProductSelection): OrderItem {
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

    private fun getOrderPaymentInfo(distance: Long, cartProducts: List<UserProductSelection>): OrderPaymentInfo {
        val deliveryFee = distance.toDouble() / 1000 * deliveryFeePerKm
        return OrderPaymentInfo(price = calculatePrice(cartProducts), deliveryFee = deliveryFee, serviceFee = serviceFee, discount = 0.0)
    }

    private fun calculatePrice(cartProducts: List<UserProductSelection>): Double {
        var totalPrice = 0.0

        for (productItem in cartProducts) {
            val product = productRepo.findById(productItem.productId).get()
            val productPrice = product.calculatePrice(productItem.attributeSelections) * productItem.num
            totalPrice += productPrice
        }

        return totalPrice
    }

    private fun getOrderEstimateRoute(addr1: Address, addr2: Address): OrderEstimatedRouteInfo{
        val merchantAddress = LatLng(addr1.lat, addr1.lng)
        val euAddress = LatLng(addr2.lat, addr2.lng)

        val result = distanceMatrix(merchantAddress, euAddress).rows.first().elements.first()
        return OrderEstimatedRouteInfo(0, result.duration.inSeconds, result.distance.inMeters, result.distance.humanReadable)
    }

    private fun distanceMatrix(addr1: LatLng, addr2: LatLng): DistanceMatrix {
        return DistanceMatrixApi.newRequest(mapApiContext)
            .origins(addr1)
            .destinations(addr2)
            .trafficModel(TrafficModel.PESSIMISTIC)
            .mode(TravelMode.DRIVING)
            .departureTime(Date().toInstant())
            .await()
    }
}

fun Double?.safeDouble() = this ?: 0.0