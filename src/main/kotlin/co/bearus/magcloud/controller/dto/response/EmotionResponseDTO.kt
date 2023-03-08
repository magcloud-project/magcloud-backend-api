package co.bearus.magcloud.controller.dto.response

import co.bearus.magcloud.domain.type.Emotion

data class EmotionResponseDTO(val emotion: Emotion, val value: Double)
