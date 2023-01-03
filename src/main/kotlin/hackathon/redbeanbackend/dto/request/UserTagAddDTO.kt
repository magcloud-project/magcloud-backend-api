package hackathon.redbeanbackend.dto.request

import jakarta.validation.constraints.NotNull

data class UserTagAddDTO(
    @field:NotNull val id: Long? = null
)
