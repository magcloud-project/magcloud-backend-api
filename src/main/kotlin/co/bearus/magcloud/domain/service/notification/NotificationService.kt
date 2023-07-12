package co.bearus.magcloud.domain.service.notification

import co.bearus.magcloud.controller.dto.response.DiaryResponseDTO
import co.bearus.magcloud.domain.repository.*
import co.bearus.magcloud.domain.type.NotificationType
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.util.*


@Service
class NotificationService(
    @Value("\${secret.google-firebase-secret-value}") val secretValue: String,
    val userDeviceRepository: JPAUserDeviceRepository,
    private val userNotificationConfigRepository: JPAUserNotificationConfigRepository,
    private val qUserFriendRepository: QUserFriendRepository,
    private val userRepository: JPAUserRepository,
) {
    init {
        val credentials = ByteArrayInputStream(Base64.getDecoder().decode(secretValue))
        val options = FirebaseOptions.builder()
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

    @Async
    fun sendDiaryCreateNotification(diary: DiaryResponseDTO) {
        val requester = userRepository.findByIdOrNull(diary.userId) ?: return
        val friends = qUserFriendRepository.getFriends(diary.userId)
        friends.forEach { user ->
            sendMessageToUser(
                notificationType = NotificationType.FEED,
                userId = user.userId,
                description = "${requester.name}님이 일기를 업로드하셨어요!",
                routePath = "/feed"
            )
        }
    }

    @Async
    fun sendMessageToUser(notificationType: NotificationType, userId: String, description: String, routePath: String = "") {
        val devices = userDeviceRepository.findAllByUserId(userId)
        if (devices.isEmpty()) return

        val config = userNotificationConfigRepository.findByIdOrNull(userId) ?: return
        if (notificationType == NotificationType.SOCIAL && !config.socialEnabled) return
        if (notificationType == NotificationType.APPLICATION && !config.appEnabled) return
        if (notificationType == NotificationType.FEED && !config.feedEnabled) return

        val message: MulticastMessage = MulticastMessage.builder()
            .putData("routePath", routePath)
            .setNotification(
                Notification.builder()
                    .setTitle(notificationType.displayName)
                    .setBody(description)
                    .build()
            )
            .addAllTokens(devices.map { it.deviceToken })
            .setApnsConfig(ApnsConfig.builder().setAps(Aps.builder().setSound("default").setContentAvailable(true).build()).build())
            .build()
        try {
            FirebaseMessaging.getInstance().sendMulticastAsync(message)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
