package com.sonnt.fp_be.features.shared.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service
class FcmService(private val firebaseMessaging: FirebaseMessaging) {
    @Throws(FirebaseMessagingException::class)
    fun sendNotification(token: String?, title: String, content: String): String {
        val notification: Notification = Notification
            .builder()
            .setTitle(title)
            .setBody(content)
            .build()
        val message: Message = Message
            .builder()
            .setToken(token)
            .setNotification(notification)
            .putAllData(mapOf("title" to title, "content" to content))
            .build()
        return firebaseMessaging.send(message)
    }
}