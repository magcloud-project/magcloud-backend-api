package hackathon.redbeanbackend.dto

data class DiaryResultDTO(val diaryId: Long, val sadness: Float, val joy: Float, val natural: Float, val anger: Float, val fear: Float, val depression: Float) {
}
