package com.sonnt.fp_be.features.sysadmin

import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.features.shared.services.BaseService
import com.sonnt.fp_be.features.shared.services.FcmService
import com.sonnt.fp_be.features.shared.services.OrderTrackingService
import com.sonnt.fp_be.model.entities.product.ProductCategory
import com.sonnt.fp_be.repos.AccountRepo
import com.sonnt.fp_be.repos.OrderRecordRepo
import com.sonnt.fp_be.repos.product.ProductCategoryRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SysAdminService: BaseService() {
    @Autowired lateinit var categoryRepo: ProductCategoryRepo
    @Autowired lateinit var orderRecordRepo: OrderRecordRepo
    @Autowired lateinit var fcmService: FcmService
    @Autowired lateinit var accountRepo: AccountRepo
    @Autowired lateinit var orderTrackingService: OrderTrackingService
    fun addCategory(categoryDTO: ProductCategoryDTO) {
        val categoryEntity = modelMapper.map(categoryDTO, ProductCategory::class.java)
        categoryRepo.save(categoryEntity)
    }

    fun deleteOrderBy(orderId: Long) {
        orderRecordRepo.deleteAllById(listOf(orderId))
        orderRecordRepo.flush()
    }

    fun pushTestNoti(username: String) {
        val fcmToken = accountRepo.findByUsername(username)?.fcmToken ?: return
        fcmService.sendNotification(fcmToken, "test", "test")
    }

    fun sendOrderCompleted(orderId: Long) {
        val order = orderRecordRepo.findById(orderId).get()
        orderTrackingService.sendOrderCompleted(order)
    }
}