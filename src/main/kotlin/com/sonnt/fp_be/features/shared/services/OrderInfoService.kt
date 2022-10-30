package com.sonnt.fp_be.features.shared.services

import com.google.maps.DistanceMatrixApi
import com.google.maps.model.DistanceMatrix
import com.google.maps.model.LatLng
import com.google.maps.model.TrafficModel
import com.google.maps.model.TravelMode
import com.sonnt.fp_be.features.enduser.order.model.OrderPaymentInfo
import com.sonnt.fp_be.features.enduser.order.model.UserProductSelection
import com.sonnt.fp_be.features.enduser.order.response.OrderInfo
import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import com.sonnt.fp_be.features.shared.model.toDTO
import com.sonnt.fp_be.model.entities.Address
import com.sonnt.fp_be.model.entities.extension.calculatePrice
import com.sonnt.fp_be.model.entities.extension.toOrderedProductItem
import com.sonnt.fp_be.model.entities.extension.toUserProductSelection
import com.sonnt.fp_be.model.entities.order.OrderEstimatedRouteInfo
import com.sonnt.fp_be.model.entities.order.OrderRecord
import com.sonnt.fp_be.repos.OrderRecordRepo
import com.sonnt.fp_be.repos.product.ProductRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderInfoService: BaseService() {
    @Autowired lateinit var orderRepo: OrderRecordRepo
    @Autowired lateinit var productRepo: ProductRepo

    val serviceFee = 2000.0
    val deliveryFeePerKm = 10000

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

        val driverInfo = order.driver?.toDTO()

        return OrderInfo(order.id,
            order.status.value,
            order.createDate,
            order.driverAcceptTime,
            modelMapper.map(fromAddress, FPAddressDTO::class.java),
            modelMapper.map(toAddress, FPAddressDTO::class.java),
            orderEstimatedRouteInfo,
            orderedItem,
            orderPaymentInfo,
            order.customer?.account?.name ?: "",
            order.customer?.account?.phone ?: "",
            order.merchant?.account?.name ?: "",
            order.merchant?.account?.phone ?: "",
            driverInfo
        )
    }

    fun getOrderPaymentInfo(distance: Long, cartProducts: List<UserProductSelection>): OrderPaymentInfo {
        val deliveryFee = distance.toDouble() / 1000 * deliveryFeePerKm
        return OrderPaymentInfo(price = calculatePrice(cartProducts), deliveryFee = deliveryFee, serviceFee = serviceFee, discount = 0.0)
    }

    fun getOrderEstimateRoute(addr1: Address, addr2: Address): OrderEstimatedRouteInfo {
        val merchantAddress = LatLng(addr1.lat, addr1.lng)
        val euAddress = LatLng(addr2.lat, addr2.lng)

        val result = distanceMatrix(merchantAddress, euAddress).rows.first().elements.first()
        return OrderEstimatedRouteInfo(0, result.duration.inSeconds, result.distance.inMeters, result.distance.humanReadable)
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