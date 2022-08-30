package com.sonnt.fp_be.features.shared.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


class HelloMessage(var username: String = "", var message: String = "")
class Greeting(var content: String = "")

@Controller
class GreetingController: BaseController() {

    @Autowired
    private lateinit var simpMessagingTemplate: SimpMessagingTemplate

    @MessageMapping("/hello")

    @Throws(Exception::class)
    fun greeting(message: HelloMessage) {
        simpMessagingTemplate.convertAndSendToUser(message.username, "/queue/messages", message)
    }

    companion object{
        var count = 0
    }
}