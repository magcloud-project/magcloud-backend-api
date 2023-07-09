package co.bearus.magcloud.controller.dto.response

data class DiaryResponseDTO(
    val diaryId: String,
    val userId: String,
    val date: String,
    val emotion: String,
    val content: String,
    val contentHash: String,
    val createdAtTs: Long,
    val updatedAtTs: Long,
)

data class DiaryIntegrityResponseDTO(
    val diaryId: String,
    val emotion: String,
    val contentHash: String,
    val createdAtTs: Long,
    val updatedAtTs: Long,
)
