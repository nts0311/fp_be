package com.sonnt.fp_be.features.enduser.home

import com.sonnt.fp_be.features.enduser.home.request.GetHomeInfoRequest
import com.sonnt.fp_be.features.enduser.home.response.GetHomeInfoResponse
import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enduser/home")
class EUHomeController: BaseController() {

    @Autowired lateinit var euHomeService: EUHomeService

    @PostMapping("info")
    fun getHomeInfo(@RequestBody request: GetHomeInfoRequest): ResponseEntity<*> {
        val homeInfo = euHomeService.getHomeInfo(request)
        val responseBody = modelMapper.map(homeInfo, GetHomeInfoResponse::class.java)

        return ok(responseBody)
    }
}