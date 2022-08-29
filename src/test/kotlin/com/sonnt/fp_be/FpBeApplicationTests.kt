package com.sonnt.fp_be

import com.sonnt.fp_be.features.merchant.product.dto.ProductAttributeDTO
import com.sonnt.fp_be.features.merchant.product.dto.ProductAttributeOptionDTO
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FpBeApplicationTests {

    @Test
    fun contextLoads() {

        val dto = ProductAttributeOptionDTO(0,"ten",10.0,1)

        val db = dto.toDbModel()

    }

}
