package co.bearus.magcloud.domain.projection

import co.bearus.magcloud.controller.dto.response.FriendDTO
import com.querydsl.core.annotations.QueryProjection

data class FriendUserProjection @QueryProjection constructor(
    val userId: String,
    val name: String,
    val tag: String,
    val profileImageUrl: String,
    val isDiaryShared: Boolean,
) {
    fun toDto() = FriendDTO(
        userId = userId,
        name = name,
        nameTag = tag,
        isDiaryShared = isDiaryShared,
        profileImageUrl = profileImageUrl,
    )
}
