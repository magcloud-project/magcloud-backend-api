package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.controller.dto.response.FeedDTO
import co.bearus.magcloud.domain.repository.QUserFeedRepository
import co.bearus.magcloud.domain.service.friend.FriendService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/feeds")
class FeedController(
    private val qUserFeedRepository: QUserFeedRepository,
    private val friendService: FriendService,
) {
    @GetMapping(params = ["size"])
    fun getMyFeed(
        @RequestUser user: WebUser,
        @RequestParam size: Int,
        @RequestParam(required = false) baseId: String?,
    ): List<FeedDTO> {
        return qUserFeedRepository
            .getUserFeed(user.userId, baseId, size)
            .map { it.toDto() }
    }

    @GetMapping(params = ["userId", "size"])
    fun getSpecificFriendFeed(
        @RequestUser user: WebUser,
        @RequestParam size: Int,
        @RequestParam userId: String,
        @RequestParam(required = false) baseId: String?,
    ): List<FeedDTO> {
        if (!friendService.isDiaryReadable(user.userId, userId) && user.userId != userId) return emptyList();
        return qUserFeedRepository
            .getFriendFeed(baseId, size, user.userId, userId)
            .map { it.toDto() }
    }
}
