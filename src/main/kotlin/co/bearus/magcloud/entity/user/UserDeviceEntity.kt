package co.bearus.magcloud.entity.user

import jakarta.persistence.*

@Entity(name = "user_device")
@IdClass(DeviceIdentity::class)
class UserDeviceEntity(
    @Id @ManyToOne @JoinColumn(name = "user_id") var user: UserEntity? = null,
    @Id var fcmToken: String
) {
    constructor() : this(null, "")

    override fun hashCode(): Int {
        return fcmToken.hashCode()
    }

    override fun equals(other: Any?) = other?.javaClass == this.javaClass && fcmToken == other
}
