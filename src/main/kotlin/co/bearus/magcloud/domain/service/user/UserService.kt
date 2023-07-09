package co.bearus.magcloud.domain.service.user

import co.bearus.magcloud.controller.dto.request.AuthRegisterDTO
import co.bearus.magcloud.controller.dto.response.UserDTO
import co.bearus.magcloud.domain.entity.user.UserEntity
import co.bearus.magcloud.domain.entity.user.UserTokenEntity
import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.exception.TokenExpiredException
import co.bearus.magcloud.domain.exception.UserNotFoundException
import co.bearus.magcloud.domain.repository.JPAUserRepository
import co.bearus.magcloud.domain.repository.JPAUserTokenRepository
import co.bearus.magcloud.domain.service.dto.TokenDTO
import co.bearus.magcloud.provider.TokenProvider
import co.bearus.magcloud.util.RandomTagGenerator
import co.bearus.magcloud.util.ULIDUtils
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: JPAUserRepository,
    private val tokenRepository: JPAUserTokenRepository,
    private val tokenProvider: TokenProvider,
) {
    @Transactional
    fun getUserInfo(userId: String): UserDTO {
        return userRepository
            .findById(userId)
            .map(UserEntity::toDto)
            .orElseThrow { UserNotFoundException() }
    }

    @Transactional
    fun onRegisterRequest(authRegisterDTO: AuthRegisterDTO): UserEntity {
        var newUser: UserEntity
        do {
            newUser = UserEntity.newInstance(
                userId = ULIDUtils.generate(),
                email = authRegisterDTO.email,
                name = authRegisterDTO.name,
                tag = RandomTagGenerator.generate(),
                profileImageUrl = "https://bsc-assets-public.s3.ap-northeast-2.amazonaws.com/default_profile.jpeg",
            )
        } while (isUserExists(newUser.name, newUser.tag))
        return userRepository.save(newUser)
    }

    fun getUserByTag(tag: String): UserEntity {
        val splitted = tag.split("#")
        if (splitted.size < 2) throw UserNotFoundException()
        return userRepository.findByNameAndTag(splitted[0], splitted[1]) ?: throw UserNotFoundException()
    }

    private fun isUserExists(name: String, tag: String) = userRepository.findByNameAndTag(name, tag) != null


    @Transactional
    fun onTokenRefreshRequest(refreshToken: String): TokenDTO {
        val userId = tokenProvider.getIdFromToken(refreshToken) ?: throw TokenExpiredException()
        val user = userRepository.findById(userId).orElseThrow { throw TokenExpiredException() }
        if (tokenRepository.existsByRefreshToken(refreshToken)) throw TokenExpiredException()
        return createToken(user)
    }

    @Transactional
    fun createToken(user: UserEntity): TokenDTO {
        val accessToken = tokenProvider.createAccessToken(user)
        val refreshToken = tokenProvider.createRefreshToken(user)
        val tokenEntity = UserTokenEntity.createNewToken(
            userId = user.userId,
            refreshToken = refreshToken,
        )
        this.tokenRepository.save(tokenEntity)
        return TokenDTO(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }
}
