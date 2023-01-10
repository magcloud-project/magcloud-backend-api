package co.bearus.magcloud.dto.response

import co.bearus.magcloud.domain.Emotion

data class EmotionResponseDTO(val emotion: Emotion, val value: Double) {
}
