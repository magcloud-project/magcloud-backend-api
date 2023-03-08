package co.bearus.magcloud.service.user

import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.type.LoginProvider
import co.bearus.magcloud.domain.entity.user.UserEntity
import co.bearus.magcloud.domain.repository.JPAUserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: JPAUserRepository,
    private val tokenService: TokenService,
    val bCrypt: BCryptPasswordEncoder
) {
    fun encrypt(password: String): String = bCrypt.encode(password)
    fun compare(password: String, encryptedPassword: String): Boolean = bCrypt.matches(password, encryptedPassword)

    fun getUserInfo(userId: Long): co.bearus.magcloud.controller.dto.response.UserDTO {
        return repository.findById(userId).map {
            co.bearus.magcloud.controller.dto.response.UserDTO(
                it.id!!,
                it.email,
                it.name
            )
        }
            .orElseThrow { throw DomainException() }
    }

    fun onRegisterRequest(authRegisterDTO: co.bearus.magcloud.controller.dto.request.AuthRegisterDTO): co.bearus.magcloud.controller.dto.response.APIResponse {
        val previousUser = getUserByEmail(authRegisterDTO.email)
        if (previousUser != null) throw DomainException("이미 존재하는 이메일입니다")
        val user = UserEntity(
            LoginProvider.LOCAL,
            authRegisterDTO.email,
            authRegisterDTO.email,
            encrypt(authRegisterDTO.password),
            authRegisterDTO.name
        )
        repository.save(user)
        return co.bearus.magcloud.controller.dto.response.APIResponse.ok("성공적으로 생성하였습니다")
    }

    fun getUserByEmail(email: String) = repository.getByProviderAndUserIdentifier(LoginProvider.LOCAL, email)

    fun onLoginRequest(loginDTO: co.bearus.magcloud.controller.dto.request.LoginDTO): co.bearus.magcloud.controller.dto.response.LoginResponseDTO {
        val user = getUserByEmail(loginDTO.email) ?: throw DomainException("아이디나 비밀번호를 확인해주세요.")
        if (!compare(loginDTO.password, user.password)) throw DomainException("아이디나 비밀번호를 확인해주세요.")
        return tokenService.createToken(user)
    }

    fun onTokenRefreshRequest(refreshToken: String): co.bearus.magcloud.controller.dto.response.LoginResponseDTO {
        val userId = tokenService.getIdFromToken(refreshToken) ?: throw DomainException("토큰이 만료되었습니다")
        val user = repository.findById(userId).orElseThrow { throw DomainException("유저가 존재하지 않습니다") }
        if (user.token?.refreshToken != refreshToken) throw DomainException("토큰이 일치하지 않습니다")
        return tokenService.createToken(user)
    }
}
