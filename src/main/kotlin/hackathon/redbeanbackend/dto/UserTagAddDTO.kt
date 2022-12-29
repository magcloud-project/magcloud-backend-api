package hackathon.redbeanbackend.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UserTagAddDTO(
    val id: Long
)
