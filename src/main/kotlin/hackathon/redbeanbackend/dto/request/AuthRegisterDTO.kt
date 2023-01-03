package hackathon.redbeanbackend.dto.request

import jakarta.validation.constraints.*

data class AuthRegisterDTO(
    @field:NotNull @field:NotEmpty(message = "이름을 입력해주세요") val name: String? = null,
    @field:Email(message = "올바른 이메일 형식이 아닙니다") val email: String? = null,
    @field:NotNull @field:Size(min = 6, max = 16, message = "비밀번호는 6~16자로 설정해주세요") val password: String? = null,
    @field:Min(10) @field:Max(200) val age: Int? = null
)
