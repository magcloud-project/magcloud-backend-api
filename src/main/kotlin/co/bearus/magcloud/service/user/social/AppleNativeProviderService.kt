package co.bearus.magcloud.service.user.social

import co.bearus.magcloud.domain.DomainException
import co.bearus.magcloud.domain.LoginProvider
import co.bearus.magcloud.dto.SocialInfoDTO
import co.bearus.magcloud.dto.request.SocialLoginDTO
import co.bearus.magcloud.dto.response.LoginResponseDTO
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.nimbusds.jose.jwk.JWK
import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*


@Service
class AppleNativeProviderService(
    private val socialService: SocialService,
) : SocialProvider {
    data class JWTTokenHeader(var kid: String, val alr: String)
    override fun login(dto: SocialLoginDTO): LoginResponseDTO {
        try {
            val header = dto.accessToken.split("\\.".toRegex()).toTypedArray()[0]
            val decodedHeader = String(Base64.getDecoder().decode(header))
            val parsedHeader = Gson().fromJson(decodedHeader, JWTTokenHeader::class.java)
            val parsed = Jwts.parserBuilder()
                .setSigningKey(getKeyFromApple(parsedHeader.kid))
                .build()
                .parseClaimsJws(dto.accessToken)
            return socialService.socialLogin(LoginProvider.APPLE, SocialInfoDTO(dto.name ?: "apple", parsed.body["sub"] as String, parsed.body["email"] as String))
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
        val key = keyObj?.keys?.firstOrNull() { it.kid == kid }?:throw DomainException("Invalid key")
        return JWK.parse(ObjectMapper().writeValueAsString(key)).toRSAKey().toRSAPublicKey()
    }
}
