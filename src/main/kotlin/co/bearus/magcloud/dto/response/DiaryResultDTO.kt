package co.bearus.magcloud.dto.response

data class DiaryResultDTO(
    val diaryId: Long,
    val stressed: Float,
    val anxious: Float,
    val normal: Float,
    val lonely: Float
) {
}
