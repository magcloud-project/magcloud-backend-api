package co.bearus.magcloud.domain.projection

import co.bearus.magcloud.controller.dto.request.DiaryCommentDTO
import co.bearus.magcloud.util.DateUtils.Companion.toEpochMillis
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class DiaryCommentProjection @QueryProjection constructor(
    val commentId: String,
    val diaryId: String,
    val userId: String,
    val username: String,
    val profileImageUrl: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    fun toDto() = DiaryCommentDTO(
        commentId = commentId,
        diaryId = diaryId,
        userId = userId,
        username = username,
        profileImageUrl = profileImageUrl,
        content = content,
        createdAtTs = createdAt.toEpochMillis(),
        updatedAtTs = updatedAt.toEpochMillis(),
    )
}
