package hackathon.redbeanbackend.entity

import jakarta.persistence.*

@Entity(name = "user_device")
@IdClass(DeviceIdentity::class)
class UserDeviceEntity(
    @Id @ManyToOne @JoinColumn(name = "user_id") var user: UserEntity? = null,
    @Id var fcmToken: String
) {
    constructor() : this(null, "")
}
