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
    val updatedAtTs: Long,
)

data class ChangeNameRequest(
    val name: String,
)

data class ProfileImageUpdateRequest(
    val profileImageUrl: String,
)

data class FeedDTO(
    val userId: String,
    val userName: String,
    val profileImageUrl: String,
    val diaryId: String,
    val mood: String,
    val ymd: String,
    val content: String,
    val isLiked: Boolean,
    val likeCount: Int,
    val createdAtTs: Long,
)
