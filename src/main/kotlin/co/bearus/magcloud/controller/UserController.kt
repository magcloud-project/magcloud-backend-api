package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestLanguage
import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.controller.dto.ResponseMessage
import co.bearus.magcloud.controller.dto.request.UserNotificationConfigDTO
import co.bearus.magcloud.controller.dto.response.APIResponse
import co.bearus.magcloud.controller.dto.response.ChangeNameRequest
import co.bearus.magcloud.controller.dto.response.ProfileImageUpdateRequest
import co.bearus.magcloud.controller.dto.response.UserDTO
import co.bearus.magcloud.domain.exception.UserNameTooLongException
import co.bearus.magcloud.domain.service.user.UserProfileImageService
import co.bearus.magcloud.domain.service.user.UserService
import co.bearus.magcloud.domain.type.ContextLanguage
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val userService: UserService,
    private val userProfileImageService: UserProfileImageService,
) {
    @GetMapping("/me")
    fun getMe(@RequestUser user: WebUser): ResponseEntity<UserDTO> {
        val result = userService.getUserInfo(user.userId)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/leave")
    fun leaveMagCloud(
        @RequestUser user: WebUser,
        @RequestLanguage language: ContextLanguage,
    ): APIResponse {
        userService.leaveMagCloud(user.userId)
        return APIResponse.ok(language, ResponseMessage.LEAVED_MAGCLOUD)
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String): ResponseEntity<UserDTO> {
        val result = userService.getUserInfo(userId)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/me/image-request")
    fun getImageRequest() = userProfileImageService.requestUploadUrl()

    @PostMapping("/me/profile-image")
    fun updateProfileImage(
        @RequestUser user: WebUser,
        @RequestBody request: ProfileImageUpdateRequest
    ) {
        userProfileImageService.changeProfileImage(
            userId = user.userId,
            imageUrl = request.profileImageUrl,
        )
    }

    @PostMapping("/me/name")
    fun updateName(
        @RequestUser user: WebUser,
        @RequestBody request: ChangeNameRequest
    ): UserDTO {
        if (request.name.length > 12) throw UserNameTooLongException()
        return userService.changeName(
            userId = user.userId,
            newName = request.name,
        )
    }

    @PatchMapping("/notification")
    fun updateNotificationConfig(
        @RequestUser user: WebUser,
        @RequestBody request: UserNotificationConfigDTO,
    ): UserNotificationConfigDTO {
        return userService.updateNotificationConfig(
            userId = user.userId,
            request = request,
        )
    }

    @GetMapping("/notification")
    fun getNotificationConfig(
        @RequestUser user: WebUser,
    ): UserNotificationConfigDTO {
        return userService.getNotificationConfig(
            userId = user.userId,
        )
    }
}
