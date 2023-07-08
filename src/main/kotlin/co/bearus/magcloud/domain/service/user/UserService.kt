package co.bearus.magcloud.domain.service.user

import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.type.LoginProvider
import co.bearus.magcloud.domain.entity.user.UserEntity
import co.bearus.magcloud.domain.entity.user.UserTokenEntity
import co.bearus.magcloud.domain.exception.UserNotFoundException
import co.bearus.magcloud.domain.repository.JPAUserRepository
import co.bearus.magcloud.domain.repository.JPAUserTokenRepository
import co.bearus.magcloud.provider.PasswordProvider
import co.bearus.magcloud.provider.TokenProvider
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: JPAUserRepository,
    private val tokenProvider: TokenProvider,
    private val passwordProvider: PasswordProvider,
    private val tokenRepository: JPAUserTokenRepository,
) {

    fun getUserInfo(userId: Long): co.bearus.magcloud.controller.dto.response.UserDTO {
        return repository.findById(userId).map {
            co.bearus.magcloud.controller.dto.response.UserDTO(
                it.id!!,
                it.email,
                it.name
            )
        }.orElseThrow { UserNotFoundException() }
    }

    fun onRegisterRequest(authRegisterDTO: co.bearus.magcloud.controller.dto.request.AuthRegisterDTO): co.bearus.magcloud.controller.dto.response.APIResponse {
        val previousUser = getUserByEmail(authRegisterDTO.email)
        if (previousUser != null) throw DomainException("이미 존재하는 이메일입니다")
        val user = UserEntity.createNewUser(
            loginProvider = LoginProvider.LOCAL,
            email = authRegisterDTO.email,
            identifier = authRegisterDTO.email,
            password = passwordProvider.encrypt(authRegisterDTO.password),
            name = authRegisterDTO.name
        )
        repository.save(user)
        return co.bearus.magcloud.controller.dto.response.APIResponse.ok("성공적으로 생성하였습니다")
    }

    fun getUserByEmail(email: String) = repository.getByProviderAndUserIdentifier(LoginProvider.LOCAL, email)

    fun onLoginRequest(loginDTO: co.bearus.magcloud.controller.dto.request.LoginDTO): co.bearus.magcloud.controller.dto.response.LoginResponseDTO {
        val user = getUserByEmail(loginDTO.email) ?: throw DomainException("아이디나 비밀번호를 확인해주세요.")
        if (!passwordProvider.compare(loginDTO.password, user.password)) throw DomainException("아이디나 비밀번호를 확인해주세요.")
        return createToken(user)
    }

    fun onTokenRefreshRequest(refreshToken: String): co.bearus.magcloud.controller.dto.response.LoginResponseDTO {
        val userId = tokenProvider.getIdFromToken(refreshToken) ?: throw DomainException("토큰이 만료되었습니다")
        val user = repository.findById(userId).orElseThrow { throw DomainException("유저가 존재하지 않습니다") }
        if (user.token?.refreshToken != refreshToken) throw DomainException("토큰이 일치하지 않습니다")
        return createToken(user)
    }

    fun createToken(user: UserEntity): co.bearus.magcloud.controller.dto.response.LoginResponseDTO {
        val accessToken = tokenProvider.createAccessToken(user)
        val refreshToken = tokenProvider.createRefreshToken(user)
        val tokenEntity =
            user.token?.apply { this.refreshToken = refreshToken } ?: UserTokenEntity(user.id, null, refreshToken)
        this.tokenRepository.save(tokenEntity)
        return co.bearus.magcloud.controller.dto.response.LoginResponseDTO(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}
