package co.bearus.magcloud.repository

import co.bearus.magcloud.entity.user.DeviceIdentity
import co.bearus.magcloud.entity.user.UserDeviceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserDeviceRepository : JpaRepository<UserDeviceEntity, DeviceIdentity> {
}
