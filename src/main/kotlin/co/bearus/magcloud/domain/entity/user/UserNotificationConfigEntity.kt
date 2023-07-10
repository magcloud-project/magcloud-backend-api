package co.bearus.magcloud.domain.entity.user

import co.bearus.magcloud.domain.entity.BaseAuditEntity
import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "user_notification_config")
class UserNotificationConfigEntity private constructor(
    @Id
    @Column(name = "user_id")
    val userId: String,

    @Column(name = "social_enabled")
    var socialEnabled: Boolean,

    @Column(name = "app_enabled")
    var appEnabled: Boolean,
) : Serializable, BaseAuditEntity() {
    companion object {
        fun newInstance(
            userId: String,
            socialEnabled: Boolean,
            appEnabled: Boolean,
        ) = UserNotificationConfigEntity(
            userId = userId,
            socialEnabled = socialEnabled,
            appEnabled = appEnabled,
        )
    }
}
