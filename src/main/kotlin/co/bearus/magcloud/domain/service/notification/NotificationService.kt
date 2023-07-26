package co.bearus.magcloud.domain.service.notification

import co.bearus.magcloud.controller.dto.request.DiaryCommentDTO
import co.bearus.magcloud.controller.dto.response.DiaryResponseDTO
import co.bearus.magcloud.domain.repository.*
import co.bearus.magcloud.domain.type.NotificationType
import co.bearus.magcloud.util.StringUtils
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
    private val diaryRepository: JPAUserDiaryRepository,
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
        val taggedPeoples = StringUtils.extractULIDFromString(diary.content)
        friends.removeIf { taggedPeoples.contains(it.userId) } //TODO: O(n^2)
        friends.forEach { user ->
            sendMessageToUser(
                notificationType = NotificationType.FEED,
                userId = user.userId,
                description = "${requester.name}님이 일기를 업로드하셨어요!",
                routePath = "/feed"
            )
        }

        taggedPeoples.forEach {
            val taggedUser = userRepository.findByIdOrNull(it) ?: return@forEach
            sendMessageToUser(
                notificationType = NotificationType.SOCIAL,
                userId = taggedUser.userId,
                description = "${requester.name}님이 당신을 일기에서 언급했어요!",
                routePath = "/feed"
            )
        }
    }

    @Async
    fun sendCommentCreateNotification(comment: DiaryCommentDTO) {
        val diary = diaryRepository.findByIdOrNull(comment.diaryId) ?: return
        val writer = userRepository.findByIdOrNull(comment.userId) ?: return
        if(comment.userId != diary.userId)
            sendMessageToUser(
                notificationType = NotificationType.SOCIAL,
                userId = diary.userId,
                description = "${writer.name}님이 내 일기에 댓글을 달았어요!",
                routePath = "/comment/${comment.diaryId}"
            )

        val taggedPeoples = StringUtils.extractULIDFromString(comment.content)
        taggedPeoples.forEach {
            val taggedUser = userRepository.findByIdOrNull(it) ?: return@forEach
            sendMessageToUser(
                notificationType = NotificationType.SOCIAL,
                userId = taggedUser.userId,
                description = "${writer.name}님이 당신을 댓글에서 언급했어요!",
                routePath = "/comment/${comment.diaryId}"
            )
        }
    }

    @Async
    fun sendDiaryLikedNotification(diaryId: String) {
        val diary = diaryRepository.findByIdOrNull(diaryId) ?: return
        sendMessageToUser(
            notificationType = NotificationType.SOCIAL,
            userId = diary.userId,
            description = "누군가 내 일기에 좋아요를 눌렀어요!",
            routePath = "/feed"
        )
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
