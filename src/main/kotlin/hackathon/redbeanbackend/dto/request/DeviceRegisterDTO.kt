package hackathon.redbeanbackend.dto.request

import jakarta.validation.constraints.NotEmpty

data class DeviceRegisterDTO(
    @field:NotEmpty val fcmToken: String? = null
)
