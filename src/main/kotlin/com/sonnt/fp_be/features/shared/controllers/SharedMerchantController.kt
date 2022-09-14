package com.sonnt.fp_be.features.shared.controllers

import com.sonnt.fp_be.features.merchant.tag.MerchantTagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/shared/productCategory")
class SharedMerchantController: BaseController() {
    @Autowired lateinit var merchantTagService: MerchantTagService
    @Autowired lateinit var merchantProductService: MerchantTagService
}