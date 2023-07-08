package co.bearus.magcloud.domain.entity.user

import co.bearus.magcloud.domain.entity.BaseAuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable

@Entity(name = "user_device")
@IdClass(UserDeviceKey::class)
class UserDeviceEntity private constructor(
    @Id
    @Column(name = "user_id")
    val userId: String,

    @Id
    @Column(name = "device_token")
    val deviceToken: String,

    @Column(name = "device_info")
    val deviceInfo: String,
) : Serializable, BaseAuditEntity() {
    companion object {
        fun createNewDevice(userId: String, deviceToken: String, deviceInfo: String) = UserDeviceEntity(
            userId = userId,
            deviceToken = deviceToken,
            deviceInfo = deviceInfo,
        )
    }
}
