package hackathon.redbeanbackend.service.user.social

import hackathon.redbeanbackend.domain.DomainException
import hackathon.redbeanbackend.domain.LoginProvider
import hackathon.redbeanbackend.dto.SocialInfoDTO
import hackathon.redbeanbackend.dto.response.LoginResponseDTO
import hackathon.redbeanbackend.entity.UserEntity
import hackathon.redbeanbackend.repository.JPAUserRepository
import hackathon.redbeanbackend.service.user.TokenService
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
        var previousUser = repository.getByProviderAndEmail(provider, socialInfoDTO.id)
        if (previousUser == null) {
            previousUser = socialLoginRegister(provider, socialInfoDTO)
        }
        return tokenService.createToken(previousUser)
    }

    fun socialLoginRegister(provider: LoginProvider, socialInfoDTO: SocialInfoDTO): UserEntity {
        val user = UserEntity(
            provider,
            socialInfoDTO.id,
            generateRandomPassword(),
            socialInfoDTO.name
        )
        return repository.save(user)
    }

    fun generateRandomPassword(): String {
        return bCrypt.encode(UUID.randomUUID().toString())
    }
}
