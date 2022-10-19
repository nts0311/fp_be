package com.sonnt.fp_be.features.shared.controllers

import com.sonnt.fp_be.features.enduser.order.EUOrderService
import com.sonnt.fp_be.features.shared.response.ProductCategoryResponse
import com.sonnt.fp_be.features.shared.services.OrderTrackingService
import com.sonnt.fp_be.features.shared.services.ProductCategoryService
import com.sonnt.fp_be.repos.DriverRepo
import com.sonnt.fp_be.repos.OrderRecordRepo
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

    @Autowired
    lateinit var orderRecordRepo: OrderRecordRepo
    @Autowired
    lateinit var driverRepo: DriverRepo
    @Autowired
    lateinit var orderTrackingService: OrderTrackingService

    @GetMapping("test-tracking")
    fun testTracking(@RequestParam state: Int) {
        val order = orderRecordRepo.findById(503).get()

        when(state) {
            1 -> {
                val driver = driverRepo.findAll().first()
                order.driver = driver
                orderRecordRepo.save(order)
                orderRecordRepo.flush()
                orderTrackingService.sendSuccessFindingDriver(order)
            }
            2 -> {
                orderTrackingService.sendDriverDeliveringToCustomer(order)
            }
        }
    }
}