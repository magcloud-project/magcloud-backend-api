package co.bearus.magcloud.controller

import co.bearus.magcloud.controller.dto.request.RefreshTokenRequestDTO
import co.bearus.magcloud.controller.dto.request.SocialLoginDTO
import co.bearus.magcloud.controller.dto.response.LoginResponseDTO
import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.service.user.UserService
import co.bearus.magcloud.domain.service.user.social.*
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
    private val appleService: AppleProviderService,
    private val nativeGoogleService: GoogleNativeProviderService,
) {
    @PostMapping("/refresh")
    fun requestRefresh(@RequestBody body: RefreshTokenRequestDTO): ResponseEntity<LoginResponseDTO> {
        val refreshResult = userService.onTokenRefreshRequest(body.refreshToken);
        return ResponseEntity.ok(
            LoginResponseDTO(
                accessToken = refreshResult.accessToken,
                refreshToken = refreshResult.refreshToken,
                isNewUser = false,
            )
        )
    }

    @PostMapping
    fun requestSocialLogin(
        @RequestBody dto: SocialLoginDTO,
    ): ResponseEntity<LoginResponseDTO> {
        val currentProvider = parseProvider(dto.provider)
        return ResponseEntity.ok(currentProvider.login(dto))
    }

    private fun parseProvider(provider: String): SocialProvider {
        return when (provider.lowercase(Locale.getDefault())) {
            "kakao" -> kakaoService
            "kakao-native" -> nativeKakaoService
            "google" -> nativeGoogleService
            "apple" -> appleService
            "apple-native" -> nativeAppleService
            else -> throw DomainException("Invalid provider")
        }
    }
}
