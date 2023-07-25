package co.bearus.magcloud.controller.dto.request

import co.bearus.magcloud.domain.type.Emotion
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class DiaryCreateDTO(
    @field:NotEmpty(message = "내용은 비어있을 수 없습니다") val content: String = "",
    @field:NotNull(message = "날짜는 비어있을 수 없습니다") @field:NotEmpty(message = "날짜는 비어있을 수 없습니다") val date: String = "",
    val emotion: Emotion = Emotion.NEUTRAL,
    val imageUrl: String? = null,
)

data class DiaryCommentCreateDTO(
    val content: String,
)

data class DiaryCommentDTO(
    val commentId: String,
    val diaryId: String,
    val userId: String,
    val username: String,
    val profileImageUrl: String,
    val content: String,
    val createdAtTs: Long,
    val updatedAtTs: Long,
)
