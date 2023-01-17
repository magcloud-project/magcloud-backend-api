package co.bearus.magcloud.dto.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class RefreshTokenRequestDTO(
    @field:NotEmpty(message = "토큰을 입력해주세요") val refreshToken: String = "",
)
