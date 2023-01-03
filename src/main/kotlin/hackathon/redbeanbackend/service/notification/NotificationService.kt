package hackathon.redbeanbackend.service.notification

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.*
import hackathon.redbeanbackend.entity.UserEntity
import hackathon.redbeanbackend.repository.JPAUserDeviceRepository
import org.springframework.stereotype.Service


@Service
class NotificationService(val userDeviceRepository: JPAUserDeviceRepository) {
    init {
        val credentials = NotificationService::class.java.classLoader.getResourceAsStream("secret.json")
            ?: throw RuntimeException("credentials not found")
        val options = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(credentials))
            .build()
        FirebaseApp.initializeApp(options)
    }

    fun broadcastMessage(title: String, description: String) {
        this.sendMessageToTopic("all", title, description)
    }

    fun sendMessageToTopic(topic: String, title: String, description: String) {
        try {
            val message = Message.builder()
                .setNotification(Notification.builder().setTitle(title).setBody(description).build())
                .setTopic(topic)
                .build()
            FirebaseMessaging.getInstance().send(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendMessageToUser(userEntity: UserEntity, title: String, description: String) {
        userEntity.devices.forEach { device ->
            try {
                val message = Message.builder()
                    .setNotification(Notification.builder().setTitle(title).setBody(description).build())
                    .setToken(device.fcmToken)
                    .build()
                FirebaseMessaging.getInstance().send(message)
            } catch (e: FirebaseMessagingException) {
                if (e.messagingErrorCode == MessagingErrorCode.UNREGISTERED) {
                    userDeviceRepository.delete(device)
                    println("Device unregistered due to fcm token invalidation")
                } else {
                    e.printStackTrace()
                }
            }
        }
    }
}
