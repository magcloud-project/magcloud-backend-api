package co.bearus.magcloud.service.user.social

import co.bearus.magcloud.domain.LoginProvider
import co.bearus.magcloud.dto.SocialInfoDTO
import co.bearus.magcloud.dto.response.LoginResponseDTO
import co.bearus.magcloud.entity.UserEntity
import co.bearus.magcloud.repository.JPAUserRepository
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
    fun socialLogin(provider: LoginProvider, socialInfoDTO: SocialInfoDTO): LoginResponseDTO {
        var previousUser = repository.getByProviderAndUserIdentifier(provider, socialInfoDTO.id)
        if (previousUser == null) {
            previousUser = socialLoginRegister(provider, socialInfoDTO)
        }
        return tokenService.createToken(previousUser)
    }

    fun socialLoginRegister(provider: LoginProvider, socialInfoDTO: SocialInfoDTO): UserEntity {
        val user = UserEntity(
            provider,
            socialInfoDTO.id,
            "",
            generateRandomPassword(),
            socialInfoDTO.name
        )
        return repository.save(user)
    }

    fun generateRandomPassword(): String {
        return bCrypt.encode(UUID.randomUUID().toString())
    }
}
