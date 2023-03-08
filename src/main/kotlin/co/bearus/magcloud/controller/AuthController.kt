package co.bearus.magcloud.controller

import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.service.user.UserService
import co.bearus.magcloud.domain.service.user.social.*
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val userService: UserService,
    private val kakaoService: KakaoProviderService,
    private val nativeKakaoService: KakaoNativeProviderService,
    private val nativeAppleService: AppleNativeProviderService,
    private val appleService: AppleProviderService
) {
    @PostMapping
    fun requestLogin(@RequestBody @Valid dto: co.bearus.magcloud.controller.dto.request.LoginDTO): ResponseEntity<co.bearus.magcloud.controller.dto.response.LoginResponseDTO> {
        return ResponseEntity.ok(userService.onLoginRequest(dto))
    }

    @PostMapping("/refresh")
    fun requestRefresh(@RequestBody body: co.bearus.magcloud.controller.dto.request.RefreshTokenRequestDTO): ResponseEntity<co.bearus.magcloud.controller.dto.response.LoginResponseDTO> {
        return ResponseEntity.ok(userService.onTokenRefreshRequest(body.refreshToken))
    }

    @PostMapping("/{provider}")
    fun requestSocialLogin(
        @RequestBody token: co.bearus.magcloud.controller.dto.request.SocialLoginDTO,
        @PathVariable provider: String
    ): ResponseEntity<co.bearus.magcloud.controller.dto.response.LoginResponseDTO> {
        val currentProvider = parseProvider(provider)
        return ResponseEntity.ok(currentProvider.login(token))
    }

    private fun parseProvider(provider: String): SocialProvider {
        return when (provider.lowercase(Locale.getDefault())) {
            "kakao" -> kakaoService
            "kakao-native" -> nativeKakaoService
            "google" -> appleService
            "apple" -> appleService
            "apple-native" -> nativeAppleService
            else -> throw DomainException("Invalid provider")
        }
    }
}
