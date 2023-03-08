package co.bearus.magcloud.domain.service.notification

import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.entity.user.DeviceIdentity
import co.bearus.magcloud.domain.entity.user.UserDeviceEntity
import co.bearus.magcloud.domain.entity.user.UserEntity
import co.bearus.magcloud.domain.repository.JPAUserDeviceRepository
import co.bearus.magcloud.domain.repository.JPAUserRepository
import org.springframework.stereotype.Service

@Service
class UserDeviceService(val userRepository: JPAUserRepository, val userDeviceRepository: JPAUserDeviceRepository) {
    fun registerDevice(
        id: Long,
        dto: co.bearus.magcloud.controller.dto.request.DeviceRegisterDTO
    ): co.bearus.magcloud.controller.dto.response.APIResponse {
        val user = userRepository.findById(id).orElseThrow { throw DomainException("User not found") }
        if (checkUserHasDevice(user, dto.fcmToken)) throw DomainException("User already registered that device")
        val device = UserDeviceEntity(user, dto.fcmToken)
        userDeviceRepository.save(device)
        return co.bearus.magcloud.controller.dto.response.APIResponse.ok("성공적으로 등록하였습니다.")
    }

    private fun checkUserHasDevice(user: UserEntity, fcmToken: String): Boolean {
        val device = userDeviceRepository.findById(DeviceIdentity(user, fcmToken))
        return device.isPresent
    }
}
