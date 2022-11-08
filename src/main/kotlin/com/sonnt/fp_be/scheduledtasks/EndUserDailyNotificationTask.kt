package com.sonnt.fp_be.scheduledtasks

import com.sonnt.fp_be.features.shared.services.FcmService
import com.sonnt.fp_be.repos.CustomerRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class EndUserDailyNotificationTask {
    val titles = listOf(
        "Bữa trưa chỉ 30K mại dzô!",
        "title 2",
        "title 3"
    )

    val contents = listOf(
        "Bánh mì, xôi, bún, cơm,... Thèm gì order luôn nhé!",
        "content 2",
        "content 3"
    )

    @Autowired lateinit var customerRepo: CustomerRepo
    @Autowired lateinit var fcmService: FcmService

    @Scheduled(cron = "0 00 10 * * ?")
    fun pushDailyNotiAtMorning() {
        pushDailyNotificationEU()
    }

    @Scheduled(cron = "0 58 15 * * ?")
    fun pushDailyNotiAtAfternoon() {
        pushDailyNotificationEU()
    }

    fun pushDailyNotificationEU() {
        println("Push daily noti EU")
        val index = Random.nextInt(0, titles.size)
        val listCustomer = customerRepo.findAll()
        listCustomer.forEach {
            fcmService.sendNotification(it.account.fcmToken, titles[index], contents[index])
        }
    }

}