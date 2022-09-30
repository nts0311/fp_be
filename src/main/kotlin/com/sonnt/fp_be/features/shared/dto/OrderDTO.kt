package com.sonnt.fp_be.features.shared.dto

import com.sonnt.fp_be.model.entities.order.OrderItemAttributeOption
import com.sonnt.fp_be.model.entities.order.OrderStatus
import com.sonnt.fp_be.model.entities.product.ProductAttribute
import java.time.LocalDateTime

data class OrderDTO(
    var id: Long = 0,
    var code: String? = null,
    var createDate: LocalDateTime? = null,
    var note: String? = null,
    var status: String? = null,
    var orderItem: List<OrderItemDTO> = listOf()
)

data class OrderItemDTO(
    var product: ProductDTO,
    var num: Int,
    var attributes: List<OrderItemAttributeDTO>
)

data class OrderItemAttributeDTO(
    var attribute: ProductAttributeDTO,
    var options: List<ProductAttributeOptionDTO>
)