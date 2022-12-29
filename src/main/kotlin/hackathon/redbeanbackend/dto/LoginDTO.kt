package hackathon.redbeanbackend.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class LoginDTO(
    @field:NotNull @field:NotEmpty @field:Email val email: String,
    @field:NotNull @field:Size(min = 6, max = 16) val password: String,
)
