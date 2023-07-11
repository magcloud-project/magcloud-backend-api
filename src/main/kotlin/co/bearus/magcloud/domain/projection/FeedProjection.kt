package co.bearus.magcloud.domain.projection

import co.bearus.magcloud.controller.dto.response.FeedDTO
import co.bearus.magcloud.util.DateUtils.Companion.toEpochMillis
import co.bearus.magcloud.util.DateUtils.Companion.toSimpleYmdFormat
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate
import java.time.LocalDateTime

data class FeedProjection @QueryProjection constructor(
    val userId: String,
    val userName: String,
    val profileImageUrl: String,
    val diaryId: String,
    val mood: String,
    val ymd: LocalDate,
    val content: String,
    val createdAt: LocalDateTime,
) {
    fun toDto() = FeedDTO(
        userId = userId,
        userName = userName,
        profileImageUrl = profileImageUrl,
        diaryId = diaryId,
        mood = mood,
        ymd = ymd.toSimpleYmdFormat(),
        content = content,
        createdAtTs = createdAt.toEpochMillis(),
    )
}
