package com.sonnt.fp_be.features.merchant.info

import com.sonnt.fp_be.features.merchant.info.dto.ChangeMerchantActivityStatusRequest
import com.sonnt.fp_be.features.merchant.order.dto.GetMerchantActiveOrdersResponse
import com.sonnt.fp_be.features.merchant.order.dto.GetMerchantDoneOrdersResponse
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/merchant/info")
class MerchantInfoController: BaseController() {

    @Autowired
    lateinit var infoService: MerchantInfoService

    @PostMapping("change-activity-status")
    fun changeActivityStatus(@RequestBody request: ChangeMerchantActivityStatusRequest): ResponseEntity<*> {
        infoService.changeActivityRequest(request)
        return ok()
    }

}
