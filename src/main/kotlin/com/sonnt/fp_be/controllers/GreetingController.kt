package com.sonnt.fp_be.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller


class HelloMessage(var name: String = "")
class Greeting(var content: String = "")

@Controller
class GreetingController: BaseController() {

    @Autowired
    private lateinit var simpMessagingTemplate: SimpMessagingTemplate



    @MessageMapping("/hello")

    @Throws(Exception::class)
    fun greeting(message: HelloMessage) {
        simpMessagingTemplate.convertAndSendToUser(username, "/queue/messages", "Helo $username-$userId, count:${count++}");
    }

    companion object{
        var count = 0
    }
}