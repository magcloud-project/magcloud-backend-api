package co.bearus.magcloud.domain.service.user.social

import co.bearus.magcloud.controller.dto.SocialInfoDTO
import co.bearus.magcloud.controller.dto.request.SocialLoginDTO
import co.bearus.magcloud.controller.dto.response.LoginResponseDTO
import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.type.LoginProvider
import co.bearus.magcloud.util.MockNickGenerator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


@Service
class GoogleNativeProviderService(
    @Qualifier("googleOAuthClient") private val webClient: WebClient,
    private val socialService: SocialService,
) : SocialProvider {

    override fun login(dto: SocialLoginDTO): LoginResponseDTO {
        try {
            val authResult = authenticate(dto.accessToken) ?: throw DomainException()
            return socialService.findUserByProviderInfo(
                LoginProvider.APPLE,
                authResult
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw DomainException()
        }
    }

    fun authenticate(token: String): SocialInfoDTO? {
        return webClient.get()
            .uri("/v1/userinfo")
            .headers {
                it.set("Authorization", "Bearer $token")
            }
            .retrieve()
            .bodyToMono(GoogleUserInfo::class.java)
            .map {
                SocialInfoDTO(
                    id = it.sub,
                    email = it.email,
                    name = it.name ?: MockNickGenerator.generate(),
                    provider = LoginProvider.GOOGLE,
                )
            }.block()
    }

    data class GoogleUserInfo(
        val sub: String,
        val name: String?,
        val email: String,
    )
}
