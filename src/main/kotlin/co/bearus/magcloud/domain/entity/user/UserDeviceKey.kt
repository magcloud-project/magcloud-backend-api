package co.bearus.magcloud.domain.entity.user

import java.io.Serializable

class UserDeviceKey(
    val userId: String = "",
    val deviceToken: String = "",
) : Serializable
