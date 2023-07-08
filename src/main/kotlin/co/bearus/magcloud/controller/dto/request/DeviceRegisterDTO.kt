package co.bearus.magcloud.controller.dto.request

import jakarta.validation.constraints.NotEmpty

data class DeviceRegisterDTO(
    @field:NotEmpty val deviceToken: String = "",
    @field:NotEmpty val deviceInfo: String = "",
)
