package co.bearus.magcloud.domain.entity.user

import java.io.Serializable

data class UserTokenEntityKey(
    val userId: String = "",
    val refreshToken: String = "",
): Serializable
