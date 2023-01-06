package hackathon.redbeanbackend.controller

import hackathon.redbeanbackend.domain.DomainException
import hackathon.redbeanbackend.domain.LoginProvider
import hackathon.redbeanbackend.dto.request.LoginDTO
import hackathon.redbeanbackend.dto.request.RefreshTokenRequestDTO
import hackathon.redbeanbackend.dto.response.LoginResponseDTO
import hackathon.redbeanbackend.service.user.UserService
import hackathon.redbeanbackend.service.user.social.KakaoProviderService
import hackathon.redbeanbackend.service.user.social.SocialProvider
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val userService: UserService,
    private val kakaoService: KakaoProviderService) {
    @PostMapping
    fun requestLogin(@RequestBody @Valid dto: LoginDTO): ResponseEntity<LoginResponseDTO> {
        return ResponseEntity.ok(userService.onLoginRequest(dto))
    }
    @PostMapping("/refresh")
    fun requestRefresh(@RequestBody body: RefreshTokenRequestDTO): ResponseEntity<LoginResponseDTO> {
        if(body.refreshToken == null){
            throw DomainException("리프레시 토큰이 없습니다.")
        }
        return ResponseEntity.ok(userService.onTokenRefreshRequest(body.refreshToken))
    }
    @PostMapping("/{provider}")
    fun requestSocialLogin(@RequestBody token: String, @PathVariable provider: String): ResponseEntity<LoginResponseDTO> {
        val currentProvider = parseProvider(provider)
        val providerService = getProviderService(currentProvider)
        return ResponseEntity.ok(providerService.login(token))
    }

    private fun parseProvider(provider: String): LoginProvider {
        return when (provider.lowercase(Locale.getDefault())) {
            "kakao" -> LoginProvider.KAKAO
            "google" -> LoginProvider.GOOGLE
            "apple" -> LoginProvider.APPLE
            else -> throw DomainException("Invalid provider")
        }
    }

    private fun getProviderService(provider: LoginProvider): SocialProvider {
        return when (provider) {
            LoginProvider.KAKAO -> kakaoService
            LoginProvider.GOOGLE -> kakaoService
            LoginProvider.APPLE -> kakaoService
            else -> throw DomainException("Invalid provider")
        }
    }
}
