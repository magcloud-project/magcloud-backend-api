package co.bearus.magcloud.controller.dto.request

import co.bearus.magcloud.domain.type.Emotion
import jakarta.validation.constraints.NotEmpty

data class DiaryPatchDTO(
    @field:NotEmpty(message = "내용은 비어있을 수 없습니다") val content: String = "",
    val emotion: Emotion = Emotion.ANGER,
)
