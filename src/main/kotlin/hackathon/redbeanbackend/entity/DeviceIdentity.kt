package hackathon.redbeanbackend.entity

import java.io.Serializable

data class DeviceIdentity(val user: UserEntity, val fcmToken: String) : Serializable {
    constructor() : this(UserEntity(), "")
}
