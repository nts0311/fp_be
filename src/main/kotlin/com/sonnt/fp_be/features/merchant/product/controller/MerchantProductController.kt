package com.sonnt.fp_be.features.merchant.product.controller

import com.sonnt.fp_be.controllers.BaseController
import com.sonnt.fp_be.features.merchant.info.services.MerchantService
import com.sonnt.fp_be.features.merchant.product.dto.ProductDTO
import com.sonnt.fp_be.features.merchant.product.service.MerchantProductService
import com.sonnt.fp_be.features.merchant.product.service.MerchantTagService
import com.sonnt.fp_be.features.shared.services.ProductCategoryService
import com.sonnt.fp_be.utils.badRequest
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/merchant/product")
class MerchantProductController: BaseController() {

    @Autowired lateinit var merchantService: MerchantService
    @Autowired lateinit var merchantProductService: MerchantProductService
    @Autowired lateinit var merchantTagService: MerchantTagService
    @Autowired lateinit var productCategoryService: ProductCategoryService

    @PostMapping("/add")
    fun addProduct(@RequestBody productDto: ProductDTO): ResponseEntity<*> {
        val a = 5 / 0
        merchantProductService.addProduct(productDto)

        return ok()
    }
}