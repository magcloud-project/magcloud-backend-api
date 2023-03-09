package co.bearus.magcloud.domain.service.user.social

import co.bearus.magcloud.domain.type.LoginProvider
import co.bearus.magcloud.domain.entity.user.UserEntity
import co.bearus.magcloud.domain.repository.JPAUserRepository
import co.bearus.magcloud.provider.PasswordProvider
import co.bearus.magcloud.provider.TokenProvider
import co.bearus.magcloud.domain.service.user.UserService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class SocialService(
    private val repository: JPAUserRepository,
    private val userService: UserService,
    private val passwordProvider: PasswordProvider,
) {
    fun socialLogin(
        provider: LoginProvider,
        socialInfoDTO: co.bearus.magcloud.controller.dto.SocialInfoDTO
    ): co.bearus.magcloud.controller.dto.response.LoginResponseDTO {
        println(socialInfoDTO)
        var previousUser = repository.getByProviderAndUserIdentifier(provider, socialInfoDTO.id)
        if (previousUser == null) {
            previousUser = socialLoginRegister(provider, socialInfoDTO)
        }
        return userService.createToken(previousUser)
    }

    fun socialLoginRegister(
        provider: LoginProvider,
        socialInfoDTO: co.bearus.magcloud.controller.dto.SocialInfoDTO
    ): UserEntity {
        val user = UserEntity.createNewUser(
            loginProvider = provider,
            identifier = socialInfoDTO.id,
            email = socialInfoDTO.email,
            password = generateRandomPassword(),
            name = socialInfoDTO.name,
        )
        return repository.save(user)
    }

    fun generateRandomPassword(): String {
        return passwordProvider.encrypt(UUID.randomUUID().toString())
    }
}
