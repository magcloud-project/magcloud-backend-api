package hackathon.redbeanbackend.dto

import jakarta.validation.constraints.NotEmpty

data class DiaryCreateDTO(
    @field:NotEmpty(message = "내용은 비어있을 수 없습니다") val content: String? = null
)
