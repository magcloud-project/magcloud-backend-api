package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestLanguage
import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.controller.dto.ResponseMessage
import co.bearus.magcloud.controller.dto.request.FriendAcceptDTO
import co.bearus.magcloud.controller.dto.request.FriendRequestDTO
import co.bearus.magcloud.controller.dto.response.APIResponse
import co.bearus.magcloud.controller.dto.response.FriendDTO
import co.bearus.magcloud.domain.service.friend.FriendService
import co.bearus.magcloud.domain.service.user.UserService
import co.bearus.magcloud.domain.type.ContextLanguage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users/friends")
class FriendController(
    private val userService: UserService,
    private val friendService: FriendService,
) {
    @GetMapping
    fun getFriends(
        @RequestUser user: WebUser,
    ): List<FriendDTO> {
        return friendService.getFriends(user.userId)
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
        val receiver = userService.getUserByTag(body.tag)
        friendService.requestFriend(user.userId, receiver.userId)
        return APIResponse.ok(language, ResponseMessage.SENT_FRIEND_REQUEST)
    }

    @PostMapping("/requests/accept")
    fun acceptFriendRequest(
        @RequestUser user: WebUser,
        @RequestBody body: FriendAcceptDTO,
        @RequestLanguage language: ContextLanguage,
    ): APIResponse {
        friendService.acceptFriendRequest(user.userId, body.userId)
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
}
