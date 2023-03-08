package co.bearus.magcloud.controller.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class AuthRegisterDTO(
    @field:NotEmpty(message = "이름을 입력해주세요") val name: String = "",
    @field:Email(message = "올바른 이메일 형식이 아닙니다") val email: String = "",
    @field:NotNull @field:Size(min = 6, max = 16, message = "비밀번호는 6~16자로 설정해주세요") val password: String = "",
)
