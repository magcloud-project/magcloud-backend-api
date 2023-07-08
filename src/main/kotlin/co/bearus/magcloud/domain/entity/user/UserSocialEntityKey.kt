package co.bearus.magcloud.domain.entity.user

import co.bearus.magcloud.domain.type.LoginProvider
import java.io.Serializable

data class UserSocialEntityKey(
    val provider: LoginProvider = LoginProvider.APPLE,
    val socialIdentifier: String = "",
) : Serializable
