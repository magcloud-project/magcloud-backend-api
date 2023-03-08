package co.bearus.magcloud.controller.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class LoginDTO(
    @field:NotEmpty(message = "이메일은 필수입니다") @field:Email(message = "이메일은 형태여야합니다") val email: String = "",
    @field:NotNull(message = "비밀번호를 확인하세요") @field:Size(
        min = 6,
        max = 16,
        message = "비밀번호를 확인하세요"
    ) val password: String = "",
)
