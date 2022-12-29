package hackathon.redbeanbackend.dto

import jakarta.validation.constraints.NotEmpty

data class TagCreateDTO(
    @field:NotEmpty val name: String? = null
)
