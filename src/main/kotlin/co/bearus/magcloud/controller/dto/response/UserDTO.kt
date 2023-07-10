package co.bearus.magcloud.controller.dto.response

data class UserDTO(
    val userId: String,
    val name: String,
    val nameTag: String,
    val profileImageUrl: String,
)

data class FriendDTO(
    val userId: String,
    val name: String,
    val nameTag: String,
    val isDiaryShared: Boolean,
    val profileImageUrl: String,
)

data class DailyUserDTO(
    val userId: String,
    val name: String,
    val nameTag: String,
    val profileImageUrl: String,
    val emotion: String,
)

data class ProfileImageUpdateRequest(
    val profileImageUrl: String,
)
