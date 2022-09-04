package com.sonnt.fp_be.features.enduser.home

import com.sonnt.fp_be.features.enduser.EndUserBaseService
import com.sonnt.fp_be.features.enduser.home.model.HomeInfo
import com.sonnt.fp_be.features.enduser.home.model.HomeSection
import com.sonnt.fp_be.features.enduser.home.request.GetHomeInfoRequest
import com.sonnt.fp_be.features.shared.model.Cord
import com.sonnt.fp_be.features.shared.dto.ProductCategoryDTO
import com.sonnt.fp_be.features.shared.dto.MerchantItemDTO
import com.sonnt.fp_be.model.entities.extension.toDTO
import com.sonnt.fp_be.model.entities.extension.toItemDTO
import com.sonnt.fp_be.model.entities.extension.toMerchantItemList
import com.sonnt.fp_be.repos.AccountRepo
import com.sonnt.fp_be.repos.CustomerRepo
import com.sonnt.fp_be.repos.MerchantRepo
import com.sonnt.fp_be.repos.product.ProductCategoryRepo
import com.sonnt.fp_be.utils.GPSUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EUHomeService: EndUserBaseService() {

    @Autowired lateinit var categoryRepo: ProductCategoryRepo
    @Autowired lateinit var merchantRepo: MerchantRepo

    fun getHomeInfo(request: GetHomeInfoRequest): HomeInfo {
        val currentUserCord = Cord(request.lat, request.long)

        return HomeInfo(
            categories = getCategories(),
            sections = listOf(getNearbyPromo(), getRecommendForUser()),
            nearbyRestaurant = getNearbyMerchant(currentUserCord)
        )
    }

    fun getCategories(): List<ProductCategoryDTO> {
        return categoryRepo.findAll().map { it.toDTO() }
    }

    fun getNearbyPromo(): HomeSection {
        val firstMerchant = merchantRepo.findAll().first().toItemDTO()
        val listMerchant = List(10) {
            firstMerchant
        }

        return HomeSection(
            name = "Khuyến mãi gần đây",
            description = "Khuyến mãi gần đây",
            listMerchant = listMerchant
        )
    }

    fun getRecommendForUser(): HomeSection {
        val firstMerchant = merchantRepo.findAll().first().toItemDTO()
        val listMerchant = List(10) {
            firstMerchant
        }

        return HomeSection(
            name = "Gợi ý cho bạn",
            description = "Gợi ý cho bạn",
            listMerchant = listMerchant
        )
    }

    fun getNearbyMerchant(cord: Cord): List<MerchantItemDTO> {
        return merchantRepo.findNearestMerchantByCord(cord.lat, cord.long)
            .toMerchantItemList()
            .onEach {dto ->
                dto.address?.also { address ->
                    dto.distance = GPSUtils.distance(cord, Cord(address.lat, address.lon))
                }
            }
    }
}