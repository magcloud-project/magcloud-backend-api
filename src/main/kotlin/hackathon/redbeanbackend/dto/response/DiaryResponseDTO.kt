package hackathon.redbeanbackend.dto.response

import java.time.LocalDateTime

data class DiaryResponseDTO(
    val id: Long,
    val content: String,
    val createdAt: LocalDateTime,
    val result: DiaryResultDTO?
)
