package co.bearus.magcloud.dto.request

import jakarta.validation.constraints.NotNull

data class UserTagAddDTO(
    @field:NotNull val id: Long = -1L
)
