package co.bearus.magcloud.domain.entity.user

import co.bearus.magcloud.domain.entity.BaseAuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
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

    @Column(name = "feed_enabled")
    var feedEnabled: Boolean,
) : Serializable, BaseAuditEntity() {
    companion object {
        fun newInstance(
            userId: String,
            socialEnabled: Boolean,
            appEnabled: Boolean,
            feedEnabled: Boolean,
        ) = UserNotificationConfigEntity(
            userId = userId,
            socialEnabled = socialEnabled,
            appEnabled = appEnabled,
            feedEnabled = feedEnabled,
        )
    }
}
