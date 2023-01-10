package co.bearus.magcloud.service.notification

import co.bearus.magcloud.domain.DomainException
import co.bearus.magcloud.dto.request.DeviceRegisterDTO
import co.bearus.magcloud.dto.response.APIResponse
import co.bearus.magcloud.entity.user.DeviceIdentity
import co.bearus.magcloud.entity.user.UserDeviceEntity
import co.bearus.magcloud.entity.user.UserEntity
import co.bearus.magcloud.repository.JPAUserDeviceRepository
import co.bearus.magcloud.repository.JPAUserRepository
import org.springframework.stereotype.Service

@Service
class UserDeviceService(val userRepository: JPAUserRepository, val userDeviceRepository: JPAUserDeviceRepository) {
    fun registerDevice(id: Long, dto: DeviceRegisterDTO): APIResponse {
        val user = userRepository.findById(id).orElseThrow { throw DomainException("User not found") }
        if (checkUserHasDevice(user, dto.fcmToken!!)) throw DomainException("User already registered that device")
        val device = UserDeviceEntity(user, dto.fcmToken)
        userDeviceRepository.save(device)
        return APIResponse.ok("성공적으로 등록하였습니다.")
    }

    private fun checkUserHasDevice(user: UserEntity, fcmToken: String): Boolean {
        val device = userDeviceRepository.findById(DeviceIdentity(user, fcmToken))
        return device.isPresent
    }
}
