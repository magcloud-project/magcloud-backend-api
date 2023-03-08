package co.bearus.magcloud.domain.service.user.social

import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.type.LoginProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.nimbusds.jose.jwk.JWK
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.security.interfaces.RSAPublicKey
import java.util.*


@Service
class AppleNativeProviderService(
    private val socialService: SocialService,
) : SocialProvider {
    data class JWTTokenHeader(var kid: String, val alr: String)

    override fun login(dto: co.bearus.magcloud.controller.dto.request.SocialLoginDTO): co.bearus.magcloud.controller.dto.response.LoginResponseDTO {
        try {
            val header = dto.accessToken.split("\\.".toRegex()).toTypedArray()[0]
            val decodedHeader = String(Base64.getDecoder().decode(header))
            val parsedHeader = Gson().fromJson(decodedHeader, JWTTokenHeader::class.java)
            val parsed = Jwts.parserBuilder()
                .setSigningKey(getKeyFromApple(parsedHeader.kid))
                .build()
                .parseClaimsJws(dto.accessToken)
            return socialService.socialLogin(
                LoginProvider.APPLE,
                co.bearus.magcloud.controller.dto.SocialInfoDTO(
                    dto.name ?: "apple",
                    parsed.body["sub"] as String,
                    parsed.body["email"] as String
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw DomainException()
        }
    }

    data class TokenResponse(
        val access_token: String,
        val token_type: String,
        val expires_in: Int,
        val refresh_token: String,
        val id_token: String
    )

    data class IdTokenPayload(
        val iss: String,
        val aud: String,
        val exp: Long,
        val iat: Long,
        val sub: String,
        val at_hash: String,
        val auth_time: String,
        val nonce_supported: Boolean
    )

    data class Key(val kty: String, val kid: String, val use: String, val alg: String, val n: String, val e: String)
    data class KeyResponse(
        val keys: List<Key>
    )

    private final fun getKeyFromApple(kid: String): RSAPublicKey {
        val restTemplate = RestTemplate()
        val keyObj = restTemplate.getForObject("https://appleid.apple.com/auth/keys", KeyResponse::class.java)
        val key = keyObj?.keys?.firstOrNull() { it.kid == kid } ?: throw DomainException("Invalid key")
        return JWK.parse(ObjectMapper().writeValueAsString(key)).toRSAKey().toRSAPublicKey()
    }
}
