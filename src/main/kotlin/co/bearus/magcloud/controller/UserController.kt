package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.controller.dto.response.ProfileImageUpdateRequest
import co.bearus.magcloud.controller.dto.response.UserDTO
import co.bearus.magcloud.domain.service.user.UserProfileImageService
import co.bearus.magcloud.domain.service.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val userService: UserService,
    private val userProfileImageService: UserProfileImageService,
) {
    @GetMapping("/me")
    fun getUser(@RequestUser user: WebUser): ResponseEntity<UserDTO> {
        val result = userService.getUserInfo(user.userId)
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
}
