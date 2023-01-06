package co.bearus.magcloud.repository

import co.bearus.magcloud.entity.DeviceIdentity
import co.bearus.magcloud.entity.UserDeviceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserDeviceRepository : JpaRepository<UserDeviceEntity, DeviceIdentity> {
}
