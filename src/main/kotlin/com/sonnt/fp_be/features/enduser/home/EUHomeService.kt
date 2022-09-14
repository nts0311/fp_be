package com.sonnt.fp_be.features.enduser.home

import com.sonnt.fp_be.features.enduser.EndUserBaseService
import com.sonnt.fp_be.features.enduser.home.model.HomeSection
import com.sonnt.fp_be.features.shared.dto.HomeBannerDTO
import com.sonnt.fp_be.features.shared.model.Cord
import com.sonnt.fp_be.features.shared.dto.MerchantItemDTO
import com.sonnt.fp_be.model.entities.extension.toItemDTO
import com.sonnt.fp_be.model.entities.extension.toMerchantItemList
import com.sonnt.fp_be.repos.MerchantRepo
import com.sonnt.fp_be.utils.GPSUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EUHomeService: EndUserBaseService() {
    @Autowired lateinit var merchantRepo: MerchantRepo

    fun getHomeSection(): List<HomeSection> {
        return  listOf(getNearbyPromo(), getRecommendForUser())
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
                    dto.distance = GPSUtils.distance(cord, Cord(address.lat, address.long))
                }
            }
    }

    fun getHomeBanner(): List<HomeBannerDTO> {
        return listOf(
            HomeBannerDTO(imageUrl = "https://i.postimg.cc/vTGr5Cgm/Rectangle-158.png", deepLink = "https://www.youtube.com/watch?v=7rnCUqFec3A"),
            HomeBannerDTO(imageUrl = "https://i.postimg.cc/RFBKz2TT/Rectangle-158-1.png", deepLink = "https://www.figma.com/file/1FzgoLxUQypQaNeX2GWIRy/CC-Portal?node-id=0%3A1"),
            HomeBannerDTO()
        )
    }
}