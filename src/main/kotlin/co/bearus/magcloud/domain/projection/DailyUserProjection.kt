package co.bearus.magcloud.domain.projection

import co.bearus.magcloud.controller.dto.response.DailyUserDTO
import com.querydsl.core.annotations.QueryProjection

data class DailyUserProjection @QueryProjection constructor(
    val userId: String,
    val name: String,
    val tag: String,
    val profileImageUrl: String,
    val emotion: String,
) {
    fun toDto() = DailyUserDTO(
        userId = userId,
        name = name,
        nameTag = "${name}#${tag}",
        profileImageUrl = profileImageUrl,
        emotion = emotion,
    )
}
