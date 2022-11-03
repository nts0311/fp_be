package com.sonnt.fp_be.features.shared.services

import com.sonnt.fp_be.features.shared.dto.GoongDistanceMatrixResponse
import com.sonnt.fp_be.model.entities.Address
import com.sonnt.fp_be.model.entities.order.OrderEstimatedRouteInfo
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder


const val API_KEY = "14kQyQuUG8R9aiItg54NOCog1xrRv9JQtnMH7WiA"

@Service
class GoongService {
    fun getOrderEstimateRoute(addr1: Address, addr2: Address): OrderEstimatedRouteInfo? {
        val baseUrl = "https://rsapi.goong.io/DistanceMatrix"
        val builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .queryParam("origins", "${addr1.lat},${addr1.lng}")
            .queryParam("destinations", "${addr2.lat},${addr2.lng}")
            .queryParam("api_key", API_KEY)

        val restTemplate = RestTemplate()
        val response = restTemplate.getForObject(builder.build().toUri(), GoongDistanceMatrixResponse::class.java) ?: return null

        val responseElement = response.rows.first().elements.first()

        return OrderEstimatedRouteInfo(
            durationInSec = (responseElement.duration?.value ?:0).toLong(),
            distanceInMeter = (responseElement.distance?.value ?:0).toLong(),
            distanceReadable = responseElement.distance?.text
        )
    }
}