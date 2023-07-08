package co.bearus.magcloud.domain.service.user

import co.bearus.magcloud.controller.dto.request.AuthRegisterDTO
import co.bearus.magcloud.controller.dto.response.LoginResponseDTO
import co.bearus.magcloud.controller.dto.response.UserDTO
import co.bearus.magcloud.domain.entity.user.UserEntity
import co.bearus.magcloud.domain.entity.user.UserTokenEntity
import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.exception.UserNotFoundException
import co.bearus.magcloud.domain.repository.JPAUserRepository
import co.bearus.magcloud.domain.repository.JPAUserTokenRepository
import co.bearus.magcloud.provider.TokenProvider
import co.bearus.magcloud.util.RandomTagGenerator
import co.bearus.magcloud.util.ULIDUtils
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: JPAUserRepository,
    private val tokenProvider: TokenProvider,
    private val tokenRepository: JPAUserTokenRepository,
) {
    @Transactional
    fun getUserInfo(userId: String): UserDTO {
        return repository.findById(userId).map {
            UserDTO(
                userId = it.userId,
                email = it.email,
                name = it.name,
                nameTag = "${it.name}#${it.tag}"
            )
        }.orElseThrow { UserNotFoundException() }
    }

    @Transactional
    fun onRegisterRequest(authRegisterDTO: AuthRegisterDTO): UserEntity {
        val user = UserEntity.newInstance(
            userId = ULIDUtils.generate(),
            email = authRegisterDTO.email,
            name = authRegisterDTO.name,
            tag = RandomTagGenerator.generate(),
        )
        return repository.save(user)
    }


    @Transactional
    fun onTokenRefreshRequest(refreshToken: String): LoginResponseDTO {
        val userId = tokenProvider.getIdFromToken(refreshToken) ?: throw DomainException("토큰이 만료되었습니다")
        val user = repository.findById(userId).orElseThrow { throw DomainException("유저가 존재하지 않습니다") }
        if (user.token?.refreshToken != refreshToken) throw DomainException("토큰이 일치하지 않습니다")
        return createToken(user)
    }

    @Transactional
    fun createToken(user: UserEntity): LoginResponseDTO {
        val accessToken = tokenProvider.createAccessToken(user)
        val refreshToken = tokenProvider.createRefreshToken(user)
        val tokenEntity =
            user.token?.apply { this.refreshToken = refreshToken } ?: UserTokenEntity.createNewToken(
                userId = user.userId,
                refreshToken = refreshToken
            )
        this.tokenRepository.save(tokenEntity)
        return LoginResponseDTO(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}
