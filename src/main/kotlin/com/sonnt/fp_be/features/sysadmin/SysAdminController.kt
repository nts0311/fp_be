package com.sonnt.fp_be.features.sysadmin

import com.sonnt.fp_be.features.shared.controllers.BaseController
import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.features.shared.response.ProductCategoryResponse
import com.sonnt.fp_be.features.shared.services.ProductCategoryService
import com.sonnt.fp_be.utils.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sysadmin/productCategory")
class SysAdminController: BaseController() {

    @Autowired lateinit var adminService: SysAdminService

    @PostMapping("/add")
    fun addCategory(@RequestBody categoryDTO: ProductCategoryDTO): ResponseEntity<*> {
        adminService.addCategory(categoryDTO)
        return ok()
    }

}