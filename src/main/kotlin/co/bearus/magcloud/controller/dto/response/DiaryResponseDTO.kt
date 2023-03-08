package co.bearus.magcloud.controller.dto.response

import java.time.LocalDateTime

data class DiaryResponseDTO(
    val id: Long,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val result: List<co.bearus.magcloud.controller.dto.response.EmotionResponseDTO>
)
