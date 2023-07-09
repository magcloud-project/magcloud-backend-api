package co.bearus.magcloud.domain.service.dto

data class TokenDTO(
    val accessToken: String,
    val refreshToken: String,
)
