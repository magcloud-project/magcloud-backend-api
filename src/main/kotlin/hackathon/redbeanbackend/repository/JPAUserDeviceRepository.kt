package hackathon.redbeanbackend.repository

import hackathon.redbeanbackend.entity.DeviceIdentity
import hackathon.redbeanbackend.entity.UserDeviceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserDeviceRepository : JpaRepository<UserDeviceEntity, DeviceIdentity> {
}
