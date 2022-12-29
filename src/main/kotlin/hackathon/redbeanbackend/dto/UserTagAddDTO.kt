package hackathon.redbeanbackend.dto

import jakarta.validation.constraints.NotNull

data class UserTagAddDTO(
    @field:NotNull val id: Long? = null
)
