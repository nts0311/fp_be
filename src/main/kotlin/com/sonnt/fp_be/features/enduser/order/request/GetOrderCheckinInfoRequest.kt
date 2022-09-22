package com.sonnt.fp_be.features.enduser.order.request

import com.sonnt.fp_be.features.enduser.order.model.CartProductItem
import com.sonnt.fp_be.features.shared.dto.FPAddressDTO

data class GetOrderCheckinInfoRequest(
    val euAddress: FPAddressDTO,
    val cartProducts: List<CartProductItem>
)