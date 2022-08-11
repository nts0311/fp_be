package com.sonnt.fp_be.config
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.core.io.Resource
import java.io.IOException
import java.io.InputStream
@Configuration
class FirebaseConfig {
    //TODO: In your case maybe something else
    @Value(value = "classpath:serviceAccount.json")
    private val serviceAccountResource: Resource? = null
    @Bean
    @Throws(IOException::class)
    fun createFireBaseApp(): FirebaseApp {
        val serviceAccount: InputStream = serviceAccountResource!!.inputStream
        val options: FirebaseOptions = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        //Add loggers
        println("Firebase config initialized")
        return FirebaseApp.initializeApp(options)
    }

    @Bean
    @DependsOn(value = ["createFireBaseApp"])
    fun createFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}