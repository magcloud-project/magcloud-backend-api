package co.bearus.magcloud.dto.response

import java.time.LocalDateTime

data class DiaryResponseDTO(
    val id: Long,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val version: Long,
    val result: List<EmotionResponseDTO>
)
