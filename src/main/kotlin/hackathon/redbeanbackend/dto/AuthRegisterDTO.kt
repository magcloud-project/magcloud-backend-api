package hackathon.redbeanbackend.dto

import hackathon.redbeanbackend.domain.Gender
import jakarta.validation.constraints.*

data class AuthRegisterDTO(
    @field:NotNull @field:NotEmpty val name: String,
    @field:Email val email: String,
    @field:NotNull @field:Size(min = 6, max = 16) val password: String,
    @field:NotNull val gender: Gender,
    @field:Min(10) @field:Max(200) val age: Int
)
