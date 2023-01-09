package com.sonnt.fp_be

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.sonnt.fp_be.features.auth.services.MerchantAuthService
import com.sonnt.fp_be.features.shared.dto.ProductDTO
import com.sonnt.fp_be.model.entities.Account
import com.sonnt.fp_be.model.entities.product.ProductCategory
import com.sonnt.fp_be.model.entities.product.ProductMenu
import com.sonnt.fp_be.repos.AccountRepo
import com.sonnt.fp_be.repos.MerchantRepo
import com.sonnt.fp_be.repos.product.ProductCategoryRepo
import com.sonnt.fp_be.repos.product.ProductMenuRepo
import com.sonnt.fp_be.repos.product.ProductRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@SpringBootApplication
class FpBeApplication: CommandLineRunner {

    @Autowired lateinit var productRepo: ProductRepo
    @Autowired lateinit var merchantRepo: MerchantRepo
    @Autowired lateinit var accountRepo: AccountRepo
    @Autowired lateinit var categoryRepo: ProductCategoryRepo
    @Autowired lateinit var merchantAuthService: MerchantAuthService
    @Autowired lateinit var menuRepo: ProductMenuRepo

    override fun run(vararg args: String?) {
        //initFakeData()
    }

    fun initFakeData() {
        val listCategory = categoryRepo.findAll()

        listCategory.forEach {category ->
            for (i in 0..30) {
                createFakeMerchant(category, i)
            }
        }
    }

    fun createFakeMerchant(category: ProductCategory, mc: Int) {
        val account = Account(
            username = "merchant${mc+100}",
            password = "123456",
            email = "merchant${mc+100}@mail.com",
            name = "${category.name} merchant ${mc+100}",
            phone = "0123456789"
        )

        merchantAuthService.registerMerchant(account)

        val merchant = merchantRepo.findMerchantByName("${category.name} merchant ${mc+100}")

        val tag = ProductMenu(name = "test", merchant = merchant)
        menuRepo.save(tag)
        menuRepo.flush()

        for (i in 0..9) {
            val productDTO = ProductDTO(
                name = "product $i",
                description = "description of product $i",
                price = 100000.0,
                imageUrl = "",
                status = "AVAILABLE",
                categoryId = category.id,
                merchantId = merchant.id,
                tagId = tag.id,
                attributes = listOf()
            )

            productRepo.save(productDTO.toDbModel())
        }
        productRepo.flush()
    }

}

fun main(args: Array<String>) {
    runApplication<FpBeApplication>(*args)
}

@Bean
@Throws(IOException::class)
fun firebaseMessaging(): FirebaseMessaging {
    val googleCredentials: GoogleCredentials = GoogleCredentials
        .fromStream(ClassPathResource("firebase-service-account.json").inputStream)
    val firebaseOptions: FirebaseOptions = FirebaseOptions
        .builder()
        .setCredentials(googleCredentials)
        .build()
    val app: FirebaseApp = FirebaseApp.initializeApp(firebaseOptions, "my-app")
    return FirebaseMessaging.getInstance(app)
}