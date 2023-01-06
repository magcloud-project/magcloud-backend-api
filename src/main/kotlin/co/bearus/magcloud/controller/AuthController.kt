package co.bearus.magcloud.controller

import co.bearus.magcloud.domain.DomainException
import co.bearus.magcloud.domain.LoginProvider
import co.bearus.magcloud.dto.request.LoginDTO
import co.bearus.magcloud.dto.request.RefreshTokenRequestDTO
import co.bearus.magcloud.dto.request.SocialLoginDTO
import co.bearus.magcloud.dto.response.LoginResponseDTO
import co.bearus.magcloud.service.user.UserService
import co.bearus.magcloud.service.user.social.AppleProviderService
import co.bearus.magcloud.service.user.social.KakaoProviderService
import co.bearus.magcloud.service.user.social.SocialProvider
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val userService: UserService,
    private val kakaoService: KakaoProviderService,
    private val appleService: AppleProviderService) {
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
    fun requestSocialLogin(@RequestBody token: SocialLoginDTO, @PathVariable provider: String): ResponseEntity<LoginResponseDTO> {
        val currentProvider = parseProvider(provider)
        val providerService = getProviderService(currentProvider)
        return ResponseEntity.ok(providerService.login(token.accessToken))
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
            LoginProvider.APPLE -> appleService
            else -> throw DomainException("Invalid provider")
        }
    }
}
