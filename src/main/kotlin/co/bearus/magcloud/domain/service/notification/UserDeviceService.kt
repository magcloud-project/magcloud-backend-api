package co.bearus.magcloud.domain.service.notification

import co.bearus.magcloud.controller.dto.request.DeviceRegisterDTO
import co.bearus.magcloud.controller.dto.response.APIResponse
import co.bearus.magcloud.domain.entity.user.UserDeviceEntity
import co.bearus.magcloud.domain.entity.user.UserDeviceKey
import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.repository.JPAUserDeviceRepository
import co.bearus.magcloud.domain.repository.JPAUserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserDeviceService(
    val userRepository: JPAUserRepository,
    val userDeviceRepository: JPAUserDeviceRepository
) {
    @Transactional
    fun registerDevice(
        userId: String,
        dto: DeviceRegisterDTO
    ): APIResponse {
        val user = userRepository
            .findById(userId)
            .orElseThrow { throw DomainException("User not found") }

        if (checkUserHasDevice(
                user.userId,
                dto.deviceToken
            )
        ) throw DomainException("User already registered that device")

        val device = UserDeviceEntity.createNewDevice(
            userId = user.userId,
            deviceToken = dto.deviceToken,
            deviceInfo = dto.deviceInfo,
        )
        userDeviceRepository.save(device)
        return APIResponse.ok("성공적으로 등록하였습니다.")
    }

    private fun checkUserHasDevice(userId: String, fcmToken: String): Boolean {
        val device = userDeviceRepository.findById(UserDeviceKey(userId, fcmToken))
        return device.isPresent
    }
}
