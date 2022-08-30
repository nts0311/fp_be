package com.sonnt.fp_be.features.merchant.tag

import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.features.merchant.product.MerchantProductService
import com.sonnt.fp_be.features.merchant.product.response.ProductDTO
import com.sonnt.fp_be.features.merchant.product.response.ProductResponse
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/merchant/tag")
class MerchantTagController : BaseController() {
    @Autowired
    lateinit var merchantTagService: MerchantTagService

    @GetMapping("list")
    fun getProductByMerchant(
        @RequestParam merchantId: Long,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<*> {
        return ok()
    }

    @PostMapping("/add")
    fun addProduct(@RequestBody productDto: ProductDTO): ResponseEntity<*> {


        return ok()
    }

    @PostMapping("/edit")
    fun editProduct(@RequestBody productDto: ProductDTO): ResponseEntity<*> {


        return ok()
    }
}