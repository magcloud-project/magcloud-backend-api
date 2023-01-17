package co.bearus.magcloud.dto.request

import jakarta.validation.constraints.NotEmpty

data class DeviceRegisterDTO(
    @field:NotEmpty val fcmToken: String = ""
)
