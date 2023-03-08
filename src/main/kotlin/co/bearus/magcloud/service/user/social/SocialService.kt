package co.bearus.magcloud.service.user.social

import co.bearus.magcloud.domain.type.LoginProvider
import co.bearus.magcloud.domain.entity.user.UserEntity
import co.bearus.magcloud.domain.repository.JPAUserRepository
import co.bearus.magcloud.service.user.TokenService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class SocialService(
    private val repository: JPAUserRepository,
    private val tokenService: TokenService,
    val bCrypt: BCryptPasswordEncoder
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
        return tokenService.createToken(previousUser)
    }

    fun socialLoginRegister(
        provider: LoginProvider,
        socialInfoDTO: co.bearus.magcloud.controller.dto.SocialInfoDTO
    ): UserEntity {
        val user = UserEntity(
            provider,
            socialInfoDTO.id,
            socialInfoDTO.email,
            generateRandomPassword(),
            socialInfoDTO.name
        )
        return repository.save(user)
    }

    fun generateRandomPassword(): String {
        return bCrypt.encode(UUID.randomUUID().toString())
    }
}
