package co.bearus.magcloud.controller.dto.request

import jakarta.validation.constraints.NotEmpty

data class RefreshTokenRequestDTO(
    @field:NotEmpty(message = "토큰을 입력해주세요") val refreshToken: String = "",
)
