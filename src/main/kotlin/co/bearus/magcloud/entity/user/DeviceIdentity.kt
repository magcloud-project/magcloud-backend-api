package co.bearus.magcloud.entity.user

import co.bearus.magcloud.entity.user.UserEntity
import java.io.Serializable

data class DeviceIdentity(val user: UserEntity, val fcmToken: String) : Serializable {
    constructor() : this(UserEntity(), "")
}
