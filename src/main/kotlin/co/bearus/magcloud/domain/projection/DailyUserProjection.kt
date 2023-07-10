package co.bearus.magcloud.domain.projection

import co.bearus.magcloud.controller.dto.response.DailyUserDTO
import co.bearus.magcloud.util.DateUtils.Companion.toEpochMillis
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class DailyUserProjection @QueryProjection constructor(
    val userId: String,
    val name: String,
    val tag: String,
    val profileImageUrl: String,
    val isDiaryAllowed: Boolean,
    val emotion: String?,
    val updatedAt: LocalDateTime?,
) {
    fun toDto() = DailyUserDTO(
        userId = userId,
        name = name,
        nameTag = "${name}#${tag}",
        profileImageUrl = profileImageUrl,
        emotion = if(emotion == null || !isDiaryAllowed) "unselected" else emotion,
        updatedAtTs = updatedAt?.toEpochMillis() ?: 0L,
    )
}
