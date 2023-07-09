package co.bearus.magcloud.controller.dto

import co.bearus.magcloud.domain.type.LoginProvider

data class SocialInfoDTO(
    val name: String,
    val id: String,
    val email: String,
    val provider: LoginProvider,
)
