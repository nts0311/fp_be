package com.sonnt.fp_be.features.enduser.home

import com.sonnt.fp_be.features.enduser.home.request.GetNearbyMerchantRequest
import com.sonnt.fp_be.features.enduser.home.response.GetHomeBannerResponse
import com.sonnt.fp_be.features.enduser.home.response.GetHomeSectionResponse
import com.sonnt.fp_be.features.enduser.home.response.NearByMerchantResponse
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.features.shared.model.Cord
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enduser/home")
class EUHomeController: BaseController() {

    @Autowired lateinit var euHomeService: EUHomeService

    @GetMapping("section")
    fun getHomeInfo(): ResponseEntity<*> {
        val sections = euHomeService.getHomeSection()
        return ok(GetHomeSectionResponse(sections))
    }

    @PostMapping("nearby-merchant")
    fun getNearbyMerchant(@RequestBody request: GetNearbyMerchantRequest): ResponseEntity<*> {
        val cord = Cord(request.lat, request.long)
        val merchants = euHomeService.getNearbyMerchant(cord)

        return ok(NearByMerchantResponse(merchants))
    }

    @GetMapping("banner")
    fun getHomeBanner(): ResponseEntity<*> {
        val banner = euHomeService.getHomeBanner()
        return ok(GetHomeBannerResponse(banner))
    }
}