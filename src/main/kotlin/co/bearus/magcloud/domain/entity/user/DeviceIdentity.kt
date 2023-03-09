package co.bearus.magcloud.domain.entity.user

import java.io.Serializable

data class DeviceIdentity(
    val user: UserEntity,
    val fcmToken: String
) : Serializable
