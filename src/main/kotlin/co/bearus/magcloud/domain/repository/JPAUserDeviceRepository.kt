package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.user.UserDeviceEntity
import co.bearus.magcloud.domain.entity.user.UserDeviceKey
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserDeviceRepository : JpaRepository<UserDeviceEntity, UserDeviceKey>
