package co.bearus.magcloud.domain.service.user

import co.bearus.magcloud.controller.dto.request.AuthRegisterDTO
import co.bearus.magcloud.controller.dto.request.UserNotificationConfigDTO
import co.bearus.magcloud.controller.dto.response.UserDTO
import co.bearus.magcloud.domain.entity.user.UserEntity
import co.bearus.magcloud.domain.entity.user.UserNotificationConfigEntity
import co.bearus.magcloud.domain.entity.user.UserTokenEntity
import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.exception.TokenExpiredException
import co.bearus.magcloud.domain.exception.UserNotFoundException
import co.bearus.magcloud.domain.repository.*
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
    private val userSocialRepository: JPAUserSocialRepository,
    private val userNotificationConfigRepository: JPAUserNotificationConfigRepository,
    private val jpaFriendRepository: JPAFriendRepository,
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
    fun changeName(userId: String, newName: String): UserDTO {
        val user = userRepository
            .findById(userId)
            .orElseThrow { UserNotFoundException() }
        user.name = newName
        return userRepository.save(user).toDto()
    }

    @Transactional
    fun leaveMagCloud(userId: String) {
        val user = userRepository
            .findById(userId)
            .orElseThrow { UserNotFoundException() }
        user.email = "${user.userId}-${user.email}"
        userSocialRepository.deleteAllByUserId(user.userId)

        jpaFriendRepository.deleteAllByFromUserId(user.userId)
        jpaFriendRepository.deleteAllByToUserId(user.userId)

        userRepository.save(user)
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
        val notificationConfig = UserNotificationConfigEntity.newInstance(
            userId = newUser.userId,
            socialEnabled = true,
            appEnabled = true,
            feedEnabled = true,
        )
        userNotificationConfigRepository.save(notificationConfig)
        return userRepository.save(newUser)
    }

    @Transactional
    fun updateNotificationConfig(userId: String, request: UserNotificationConfigDTO): UserNotificationConfigDTO {
        val config = userNotificationConfigRepository
            .findById(userId)
            .orElseThrow { throw UserNotFoundException() }
        config.appEnabled = request.app
        config.socialEnabled = request.social
        config.feedEnabled = request.feed
        val saveResult = userNotificationConfigRepository.save(config)
        return UserNotificationConfigDTO(
            app = saveResult.appEnabled,
            social = saveResult.socialEnabled,
            feed = saveResult.feedEnabled,
        )
    }

    fun getNotificationConfig(userId: String) = userNotificationConfigRepository
        .findById(userId)
        .map {
            UserNotificationConfigDTO(
                app = it.appEnabled,
                social = it.socialEnabled,
                feed = it.feedEnabled,
            )
        }
        .orElseThrow { throw UserNotFoundException() }

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
