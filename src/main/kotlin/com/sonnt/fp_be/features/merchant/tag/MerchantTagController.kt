package com.sonnt.fp_be.features.merchant.tag

import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.features.shared.dto.ProductMenuDTO
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/merchant/menu")
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

    @GetMapping("list-all")
    fun getAllMerchantMenu(): ResponseEntity<*> {
        val menus = merchantTagService.getProductMenu()
        return ok(ProductTagListResponse(null, null, menus))
    }

    @PostMapping("add")
    fun addTag(@RequestBody productMenuDto: ProductMenuDTO): ResponseEntity<*> {
        val result = merchantTagService.addTag(productMenuDto)
        return ok(ProductTagResponse(result))
    }

    @PostMapping("edit")
    fun editTag(@RequestBody productMenuDto: ProductMenuDTO): ResponseEntity<*> {
        merchantTagService.updateTag(productMenuDto)
        return ok()
    }

    @GetMapping("delete")
    fun deleteTag(@RequestParam tagId: Long): ResponseEntity<*> {
        merchantTagService.deleteTag(tagId)
        return ok()
    }
}