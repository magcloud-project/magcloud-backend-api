package co.bearus.magcloud.controller.dto.request

import co.bearus.magcloud.domain.type.LoginProvider

data class SocialLoginDTO(
    val provider: String,
    val accessToken: String,
)
