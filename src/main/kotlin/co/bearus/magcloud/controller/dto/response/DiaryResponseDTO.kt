package co.bearus.magcloud.controller.dto.response

data class DiaryResponseDTO(
    val diaryId: String,
    val userId: String,
    val date: String,
    val emotion: String,
    val content: String,
    val contentHash: String,
    val createdAt: Long,
    val updatedAt: Long,
)
