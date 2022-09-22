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
import com.sonnt.fp_be.features.enduser.order.request.GetOrderCheckinInfoRequest
import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import com.sonnt.fp_be.features.shared.services.mapApiContext
import com.sonnt.fp_be.model.entities.Address
import com.sonnt.fp_be.model.entities.extension.calculatePrice
import com.sonnt.fp_be.repos.product.ProductRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

@Service
class EUOrderService: EndUserBaseService() {

    @Autowired lateinit var productRepo: ProductRepo

    val serviceFee = 2000.0
    val deliveryFeePerKm = 10000

    fun getOrderCheckinInfo(request: GetOrderCheckinInfoRequest): OrderCheckinInfo {
        val cartProducts = request.cartProducts
        if(cartProducts.isEmpty()) throw BusinessException(FPResponseStatus.cartEmpty)

        val merchant = productRepo.findById(cartProducts.first().productId).get().merchant
        val merchantAddress = merchant.address ?: throw BusinessException(FPResponseStatus.notFoung)
        val euAddress = request.euAddress.toDbModel()

        val orderEstimatedRouteInfo = getOrderEstimateRoute(merchantAddress, euAddress)
        val orderPaymentInfo = getOrderPaymentInfo(orderEstimatedRouteInfo.distanceInMeter, request.cartProducts)

        return OrderCheckinInfo(orderEstimatedRouteInfo, orderPaymentInfo)
    }

    fun getOrderPaymentInfo(distance: Long, cartProducts: List<CartProductItem>): OrderPaymentInfo {
        val deliveryFee = distance.toDouble() / 1000 * deliveryFeePerKm
        return OrderPaymentInfo(price = calculatePrice(cartProducts), deliveryFee = deliveryFee, serviceFee = serviceFee, discount = 0.0)
    }

    fun calculatePrice(cartProducts: List<CartProductItem>): Double {
        var totalPrice = 0.0

        for (productItem in cartProducts) {
            val product = productRepo.findById(productItem.productId).get()
            val productPrice = product.calculatePrice(productItem.attributes) * productItem.num
            totalPrice += productPrice
        }

        return totalPrice
    }

    fun getOrderEstimateRoute(addr1: Address, addr2: Address): OrderEstimatedRouteInfo{
        val merchantAddress = LatLng(addr1.lat, addr1.long)
        val euAddress = LatLng(addr2.lat, addr2.long)

        val result = distanceMatrix(merchantAddress, euAddress).rows.first().elements.first()
        return OrderEstimatedRouteInfo(result.duration.inSeconds, result.distance.inMeters, result.distance.humanReadable)
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