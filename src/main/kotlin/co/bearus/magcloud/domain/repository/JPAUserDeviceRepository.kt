package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.user.DeviceIdentity
import co.bearus.magcloud.domain.entity.user.UserDeviceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserDeviceRepository : JpaRepository<UserDeviceEntity, DeviceIdentity> {
}
