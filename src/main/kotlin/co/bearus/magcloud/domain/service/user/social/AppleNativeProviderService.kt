package co.bearus.magcloud.domain.service.user.social

import co.bearus.magcloud.controller.dto.SocialInfoDTO
import co.bearus.magcloud.controller.dto.request.SocialLoginDTO
import co.bearus.magcloud.controller.dto.response.LoginResponseDTO
import co.bearus.magcloud.domain.exception.AuthFailedException
import co.bearus.magcloud.domain.type.LoginProvider
import co.bearus.magcloud.util.MockNickGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.jwk.JWK
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.util.*


@Service
class AppleNativeProviderService(
    @Qualifier("appleOAuthClient") private val webClient: WebClient,
    private val socialService: SocialService,
    private val objectMapper: ObjectMapper,
) : SocialProvider {

    override fun login(dto: SocialLoginDTO): LoginResponseDTO {
        val authResult = authenticate(dto.accessToken) ?: throw AuthFailedException()
        return socialService.findUserByProviderInfo(
            LoginProvider.APPLE,
            authResult
        )
    }

    fun authenticate(token: String): SocialInfoDTO? {
        return webClient.get()
            .uri("auth/keys")
            .headers {
                it.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
            }
            .retrieve()
            .bodyToMono(KeyResponse::class.java)
            .map {
                val header = token.split("\\.".toRegex()).toTypedArray()[0]
                val decodedHeader = String(Base64.getDecoder().decode(header))
                val parsedHeader = objectMapper.readValue(decodedHeader, JWTTokenHeader::class.java)
                val key = it?.keys?.first { it.kid == parsedHeader.kid }
                val keyData = JWK.parse(ObjectMapper().writeValueAsString(key)).toRSAKey().toRSAPublicKey()
                val parsed = Jwts.parserBuilder()
                    .setSigningKey(keyData)
                    .build()
                    .parseClaimsJws(token)

                SocialInfoDTO(
                    id = parsed.body["sub"] as String,
                    email = parsed.body["email"] as? String ?: "",
                    provider = LoginProvider.APPLE,
                    name = MockNickGenerator.generate(),
                )
            }.block()
    }

    data class Key(val kty: String, val kid: String, val use: String, val alg: String, val n: String, val e: String)
    data class KeyResponse(
        val keys: List<Key>,
    )

    data class JWTTokenHeader(var kid: String, val alg: String)
}
