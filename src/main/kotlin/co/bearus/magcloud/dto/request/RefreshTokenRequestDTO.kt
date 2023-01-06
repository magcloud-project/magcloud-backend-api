package co.bearus.magcloud.dto.request

import jakarta.validation.constraints.*

data class RefreshTokenRequestDTO(
    @field:NotNull @field:NotEmpty(message = "토큰을 입력해주세요") val refreshToken: String? = null,
)
