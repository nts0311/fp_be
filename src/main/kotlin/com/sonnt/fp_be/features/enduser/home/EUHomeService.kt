package com.sonnt.fp_be.features.enduser.home

import com.sonnt.fp_be.features.enduser.EndUserBaseService
import com.sonnt.fp_be.features.enduser.home.model.HomeSection
import com.sonnt.fp_be.features.shared.dto.HomeBannerDTO
import com.sonnt.fp_be.features.shared.model.Cord
import com.sonnt.fp_be.features.shared.dto.MerchantItemDTO
import com.sonnt.fp_be.model.entities.Merchant
import com.sonnt.fp_be.model.entities.extension.toItemDTO
import com.sonnt.fp_be.model.entities.extension.toMerchantItemList
import com.sonnt.fp_be.repos.MerchantRepo
import com.sonnt.fp_be.utils.GPSUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam

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
                    dto.distance = GPSUtils.distance(cord, Cord(address.lat, address.lng))
                }
            }
    }

    fun getHomeBanner(): List<HomeBannerDTO> {
        return listOf(
            HomeBannerDTO(imageUrl = "https://images.foody.vn/images/blogs/foody-upload-api-foody-1170x370%20(7)-637196926940136811-200313103813.jpg", deepLink = "https://www.youtube.com/watch?v=7rnCUqFec3A"),
            HomeBannerDTO(imageUrl = "https://xinmagiamgia.com/wp-content/uploads/2020/02/Screenshot_10.jpg", deepLink = "https://www.figma.com/file/1FzgoLxUQypQaNeX2GWIRy/CC-Portal?node-id=0%3A1"),
            HomeBannerDTO(imageUrl = "https://media.thanhnienviet.vn/uploads/2021/04/30/bia-1619752854.jpg")
        )
    }

    fun findMerchant(categoryId: Long?, searchKey: String?, page: Int, size: Int): List<MerchantItemDTO> {
        val page = PageRequest.of(page, size)

        val listMerchant = if (categoryId == null) {
            if (searchKey == null)
                return listOf()
            else
                merchantRepo.findMerchantsWithName(searchKey, page)
        } else {
            if (searchKey == null)
                merchantRepo.findMerchantsWithCategory(categoryId, "",page)
            else
                merchantRepo.findMerchantsWithCategory(categoryId, searchKey, page)
        }

        val cord = super.customerRepo.findCustomerByAccountId(userId)?.currentAddress?.let { Cord(it.lat, it.lng) }

        return if (cord != null) {
            listMerchant.toMerchantItemList().onEach {dto ->
                dto.address?.also { address ->
                    dto.distance = GPSUtils.distance(cord, Cord(address.lat, address.lng))
                }
            }
        } else {
            listMerchant.toMerchantItemList()
        }
    }

}