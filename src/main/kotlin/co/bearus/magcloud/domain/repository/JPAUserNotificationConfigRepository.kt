package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.user.UserNotificationConfigEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserNotificationConfigRepository : JpaRepository<UserNotificationConfigEntity, String>
