package com.sonnt.fp_be.features.enduser.info

import com.sonnt.fp_be.features.enduser.home.request.GetNearbyMerchantRequest
import com.sonnt.fp_be.features.enduser.home.response.GetHomeSectionResponse
import com.sonnt.fp_be.features.enduser.home.response.NearByMerchantResponse
import com.sonnt.fp_be.features.enduser.info.response.CurrentLocationResponse
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.features.shared.dto.FPAddressDTO
import com.sonnt.fp_be.features.shared.model.Cord
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enduser/info")
class EUInfoController: BaseController() {

    @Autowired lateinit var euInfoService: EUInfoService

    @PostMapping("current-address")
    fun getUserLocation(@RequestBody address: FPAddressDTO): ResponseEntity<*> {
        return ok(CurrentLocationResponse(euInfoService.getUserLocation(address)))
    }
}