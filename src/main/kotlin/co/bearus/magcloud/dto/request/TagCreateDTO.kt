package co.bearus.magcloud.dto.request

import jakarta.validation.constraints.NotEmpty

data class TagCreateDTO(
    @field:NotEmpty val name: String = ""
)
