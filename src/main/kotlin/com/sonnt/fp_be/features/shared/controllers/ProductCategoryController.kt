package com.sonnt.fp_be.features.shared.controllers

import com.sonnt.fp_be.features.shared.response.ProductCategoryResponse
import com.sonnt.fp_be.features.shared.services.ProductCategoryService
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/shared/productCategory")
class ProductCategoryController: BaseController() {

    @Autowired lateinit var categoryService: ProductCategoryService

    @GetMapping("list")
    fun getProductCategoryList(): ResponseEntity<*> {
        val categoryList = categoryService.getCategories()
        return ok(ProductCategoryResponse(categoryList))
    }


}