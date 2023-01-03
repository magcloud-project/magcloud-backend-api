package hackathon.redbeanbackend.service.notification

import hackathon.redbeanbackend.domain.DomainException
import hackathon.redbeanbackend.dto.request.DeviceRegisterDTO
import hackathon.redbeanbackend.dto.response.APIResponse
import hackathon.redbeanbackend.entity.DeviceIdentity
import hackathon.redbeanbackend.entity.UserDeviceEntity
import hackathon.redbeanbackend.entity.UserEntity
import hackathon.redbeanbackend.repository.JPAUserDeviceRepository
import hackathon.redbeanbackend.repository.JPAUserRepository
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
