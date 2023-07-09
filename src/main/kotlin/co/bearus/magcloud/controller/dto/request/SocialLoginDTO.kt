package co.bearus.magcloud.controller.dto.request

data class SocialLoginDTO(
    val provider: String,
    val accessToken: String,
)
