package com.sonnt.fp_be.features.shared.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.sonnt.fp_be.features.shared.model.WSMessage
import com.sonnt.fp_be.features.shared.model.WSMessageCode
import com.sonnt.fp_be.features.shared.model.WSMessageWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class WSMessageService {
    @Autowired
    private lateinit var messageTemplate: SimpMessagingTemplate
    private val objectMapper = ObjectMapper().apply {
        registerModule(JavaTimeModule())
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    fun <T> sendMessage(messageWrapper: WSMessageWrapper<T>) {
        val body = objectMapper.writeValueAsString(messageWrapper.body)
        val message = WSMessage(messageWrapper.code, body)
        messageTemplate.convertAndSendToUser(messageWrapper.username, messageWrapper.endpoint, message)
    }

    fun sendTestMessage(username: String) {
        messageTemplate.convertAndSendToUser("driver", "/driver/test", WSMessage(WSMessageCode.NEW_ORDER, "test"))
    }

}