package co.bearus.magcloud.entity.user

import java.io.Serializable

data class DeviceIdentity(val user: UserEntity, val fcmToken: String) : Serializable {
    constructor() : this(UserEntity(), "")
}
