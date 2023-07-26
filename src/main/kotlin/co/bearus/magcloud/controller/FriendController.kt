package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestLanguage
import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.controller.dto.ResponseMessage
import co.bearus.magcloud.controller.dto.request.FriendAcceptDTO
import co.bearus.magcloud.controller.dto.request.FriendRequestDTO
import co.bearus.magcloud.controller.dto.response.*
import co.bearus.magcloud.domain.exception.CannotRequestToMyselfException
import co.bearus.magcloud.domain.service.friend.FriendService
import co.bearus.magcloud.domain.service.notification.NotificationService
import co.bearus.magcloud.domain.service.user.UserService
import co.bearus.magcloud.domain.type.ContextLanguage
import co.bearus.magcloud.domain.type.NotificationType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users/friends")
class FriendController(
    private val userService: UserService,
    private val friendService: FriendService,
    private val notificationService: NotificationService,
) {
    @GetMapping
    fun getFriends(
        @RequestUser user: WebUser,
    ): List<FriendDTO> {
        return friendService.getFriends(user.userId)
    }

    @GetMapping("/daily")
    fun getDailyFriends(
        @RequestUser user: WebUser,
    ): List<DailyUserDTO> {
        return friendService
            .getDailyFriends(user.userId)
            .sortedByDescending { it.updatedAtTs }
    }

    @PatchMapping("/shareable")
    fun updateToShareable(
        @RequestUser user: WebUser,
        @RequestBody body: FriendAcceptDTO,
        @RequestLanguage language: ContextLanguage,
    ): APIResponse {
        friendService.updateFriendShareableStatus(user.userId, body.userId, true)
        return APIResponse.ok(language, ResponseMessage.SHARED_DIARY)
    }

    @PatchMapping("/unshareable")
    fun updateToUnShareable(
        @RequestUser user: WebUser,
        @RequestBody body: FriendAcceptDTO,
        @RequestLanguage language: ContextLanguage,
    ): APIResponse {
        friendService.updateFriendShareableStatus(user.userId, body.userId, false)
        return APIResponse.ok(language, ResponseMessage.HIDE_DIARY)
    }

    @PostMapping("/break")
    fun breakFriend(
        @RequestUser user: WebUser,
        @RequestBody body: FriendAcceptDTO,
        @RequestLanguage language: ContextLanguage,
    ): APIResponse {
        friendService.breakFriend(user.userId, body.userId)
        return APIResponse.ok(language, ResponseMessage.BROKE_FRIEND)
    }

    @PostMapping("/requests")
    fun requestFriend(
        @RequestUser user: WebUser,
        @RequestBody body: FriendRequestDTO,
        @RequestLanguage language: ContextLanguage,
    ): APIResponse {
        val sender = userService.getUserInfo(user.userId)
        val receiver = userService.getUserByTag(body.tag)

        if (sender.userId == receiver.userId) throw CannotRequestToMyselfException()

        friendService.requestFriend(user.userId, receiver.userId)
        notificationService.sendMessageToUser(
            notificationType = NotificationType.SOCIAL,
            userId = receiver.userId,
            description = "${sender.name}님께서 친구 요청을 보내셨습니다",
            routePath = "/friends/requests"
        )
        return APIResponse.ok(language, ResponseMessage.SENT_FRIEND_REQUEST)
    }

    @GetMapping("/requests")
    fun getFriendRequestsReceived(
        @RequestUser user: WebUser,
    ): List<UserDTO> {
        return friendService.getFriendRequests(user.userId)
    }

    @GetMapping("/requests/count")
    fun getFriendRequestsCount(
        @RequestUser user: WebUser,
    ): CountResponse {
        return CountResponse(friendService.countFriendRequests(user.userId))
    }

    @GetMapping("/requests/sent")
    fun getFriendRequestsSent(
        @RequestUser user: WebUser,
    ): List<UserDTO> {
        return friendService.getSentFriendRequests(user.userId)
    }

    @PostMapping("/requests/accept")
    fun acceptFriendRequest(
        @RequestUser user: WebUser,
        @RequestBody body: FriendAcceptDTO,
        @RequestLanguage language: ContextLanguage,
    ): APIResponse {
        val sender = userService.getUserInfo(user.userId)
        val receiver = userService.getUserInfo(body.userId)
        friendService.acceptFriendRequest(user.userId, body.userId)
        notificationService.sendMessageToUser(
            notificationType = NotificationType.SOCIAL,
            userId = receiver.userId,
            description = "${sender.name}님께서 친구 요청을 수락하셨습니다",
            routePath = "/friend/${sender.userId}"
        )
        return APIResponse.ok(language, ResponseMessage.ACCEPTED_FRIEND_REQUEST)
    }

    @PostMapping("/requests/deny")
    fun denyFriendRequest(
        @RequestUser user: WebUser,
        @RequestBody body: FriendAcceptDTO,
        @RequestLanguage language: ContextLanguage,
    ): APIResponse {
        friendService.rejectFriendRequest(user.userId, body.userId)
        return APIResponse.ok(language, ResponseMessage.DENIED_FRIEND_REQUEST)
    }

    @PostMapping("/requests/cancel")
    fun cancelFriendRequest(
        @RequestUser user: WebUser,
        @RequestBody body: FriendAcceptDTO,
        @RequestLanguage language: ContextLanguage,
    ): APIResponse {
        friendService.rejectFriendRequest(body.userId, user.userId)
        return APIResponse.ok(language, ResponseMessage.CANCELLED_FRIEND_REQUEST)
    }
}
