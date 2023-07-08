package co.bearus.magcloud.domain.entity.user

import jakarta.persistence.*

@Entity(name = "user_device")
@IdClass(DeviceIdentity::class)
class UserDeviceEntity(
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity? = null,

    @Id
    @Column(name = "fcm_token")
    var fcmToken: String
) {

    override fun hashCode(): Int {
        return fcmToken.hashCode()
    }

    override fun equals(other: Any?) = other?.javaClass == this.javaClass && fcmToken == other
}
