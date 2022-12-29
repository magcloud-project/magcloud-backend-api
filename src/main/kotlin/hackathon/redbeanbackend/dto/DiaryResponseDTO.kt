package hackathon.redbeanbackend.dto

import java.time.LocalDateTime

data class DiaryResponseDTO(
    val id: Long,
    val content: String,
    val createdAt: LocalDateTime
)
