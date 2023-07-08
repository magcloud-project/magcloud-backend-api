package co.bearus.magcloud.domain.service.user.social

import co.bearus.magcloud.controller.dto.SocialInfoDTO
import co.bearus.magcloud.controller.dto.request.AuthRegisterDTO
import co.bearus.magcloud.controller.dto.response.LoginResponseDTO
import co.bearus.magcloud.domain.entity.user.UserEntity
import co.bearus.magcloud.domain.entity.user.UserSocialEntity
import co.bearus.magcloud.domain.entity.user.UserSocialEntityKey
import co.bearus.magcloud.domain.repository.JPAUserRepository
import co.bearus.magcloud.domain.repository.JPAUserSocialRepository
import co.bearus.magcloud.domain.service.user.UserService
import co.bearus.magcloud.domain.type.LoginProvider
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SocialService(
    private val userRepository: JPAUserRepository,
    private val userSocialRepository: JPAUserSocialRepository,
    private val userService: UserService,
) {
    @Transactional
    fun findUserByProviderInfo(
        provider: LoginProvider,
        socialInfoDTO: SocialInfoDTO
    ): LoginResponseDTO {
        val previousUser = userSocialRepository
            .findById(UserSocialEntityKey(provider, socialInfoDTO.id))
            .flatMap { userRepository.findById(it.userId) }
            .orElse(socialLoginRegister(provider, socialInfoDTO))
        return userService.createToken(previousUser)
    }

    private fun socialLoginRegister(
        provider: LoginProvider,
        socialInfoDTO: SocialInfoDTO
    ): UserEntity {
        val user = userService.onRegisterRequest(
            AuthRegisterDTO(
                email = socialInfoDTO.email,
                name = socialInfoDTO.name,
            )
        )
        val socialEntity = UserSocialEntity.newInstance(
            provider = provider,
            socialIdentifier = socialInfoDTO.id,
            userId = user.userId,
        )
        userSocialRepository.save(socialEntity)
        return user
    }
}
