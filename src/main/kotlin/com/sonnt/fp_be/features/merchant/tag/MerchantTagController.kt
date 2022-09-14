package com.sonnt.fp_be.features.merchant.tag

import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.features.shared.dto.ProductTagDTO
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
        val result = merchantTagService.getMerchantTag(merchantId, page, size)
        return ok(ProductTagListResponse(page, result.size, result))
    }

    @PostMapping("/add")
    fun addTag(@RequestBody productDto: ProductTagDTO): ResponseEntity<*> {
        val result = merchantTagService.addTag(productDto)
        return ok(ProductTagResponse(result))
    }

    @PostMapping("/edit")
    fun editTag(@RequestBody productDto: ProductTagDTO): ResponseEntity<*> {
        merchantTagService.updateTag(productDto)
        return ok()
    }

    @GetMapping("/delete")
    fun deleteTag(@RequestParam tagId: Long): ResponseEntity<*> {
        merchantTagService.deleteTag(tagId)
        return ok()
    }
}