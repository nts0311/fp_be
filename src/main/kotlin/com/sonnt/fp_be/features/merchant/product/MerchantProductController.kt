package com.sonnt.fp_be.features.merchant.product

import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.features.merchant.product.response.ProductDTO
import com.sonnt.fp_be.features.merchant.product.response.ProductResponse
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/merchant/product")
class MerchantProductController : BaseController() {
    @Autowired
    lateinit var merchantProductService: MerchantProductService

    @GetMapping("list")
    fun getProductByMerchant(
        @RequestParam merchantId: Long,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<*> {
        val productList = merchantProductService.getProductByMerchantId(merchantId, page, size)
        val responseBody = ProductResponse(page, size, productList)
        return ok(responseBody)
    }

    @PostMapping("/add")
    fun addProduct(@RequestBody productDto: ProductDTO): ResponseEntity<*> {
        merchantProductService.addProduct(productDto)

        return ok()
    }

    @PostMapping("/edit")
    fun editProduct(@RequestBody productDto: ProductDTO): ResponseEntity<*> {
        merchantProductService.editProduct(productDto)

        return ok()
    }
}