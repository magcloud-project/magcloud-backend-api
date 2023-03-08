package co.bearus.magcloud.domain.service.user.social

import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.type.LoginProvider
import com.google.gson.Gson
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
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*


@Service
class AppleProviderService(
    private val socialService: SocialService,
    @Value("\${secret.apple-key-id}") val appleKeyId: String,
    @Value("\${secret.apple-keyfile-value}") val appleKeyValue: String,
    @Value("\${secret.apple-team-id}") val appleTeamId: String,
    @Value("\${secret.apple-client-id}") val appleClientId: String
) : SocialProvider {
    val pKey: PrivateKey = getPrivateKey()
    override fun login(dto: co.bearus.magcloud.controller.dto.request.SocialLoginDTO): co.bearus.magcloud.controller.dto.response.LoginResponseDTO {
        try {
            val socialLoginDto = getUserInfoByClientSecret(dto)
            return socialService.socialLogin(LoginProvider.APPLE, socialLoginDto)
        } catch (e: Exception) {
            e.printStackTrace()
            throw DomainException()
        }
    }

    fun getUserInfoByClientSecret(dto: co.bearus.magcloud.controller.dto.request.SocialLoginDTO): co.bearus.magcloud.controller.dto.SocialInfoDTO {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val params: LinkedMultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("grant_type", "authorization_code")
        params.add("code", dto.accessToken)
        params.add("client_id", this.appleClientId)
        params.add("client_secret", this.generateSecretKey())

        val request = HttpEntity(params, headers)
        val url = "https://appleid.apple.com/auth/token"
        val response: ResponseEntity<String> = restTemplate.postForEntity(url, request, String::class.java)
        val responseBody = response.body

        val tokenResponse = Gson().fromJson(responseBody, TokenResponse::class.java)
        val payload =
            tokenResponse.id_token.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1]

        val decoded = String(Decoders.BASE64.decode(payload))

        val tokenPayload = Gson().fromJson(decoded, IdTokenPayload::class.java)
        return co.bearus.magcloud.controller.dto.SocialInfoDTO(dto.name ?: "apple", tokenPayload.sub, "")
    }

    fun generateSecretKey(): String {
        return Jwts.builder()
            .setHeaderParam(JwsHeader.KEY_ID, appleKeyId)
            .setIssuer(appleTeamId)
            .setAudience("https://appleid.apple.com")
            .setSubject(appleClientId)
            .setExpiration(Date(System.currentTimeMillis() + 10000 * 60 * 5))
            .setIssuedAt(Date(System.currentTimeMillis()))
            .signWith(pKey, SignatureAlgorithm.ES256)
            .compact()
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

    private final fun getPrivateKey(): PrivateKey {
        val content = String(Base64.getDecoder().decode(appleKeyValue))
        return try {
            val privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\\s+".toRegex(), "")
            val kf: KeyFactory = KeyFactory.getInstance("EC")
            kf.generatePrivate(PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)))
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Java did not support the algorithm:EC", e)
        } catch (e: InvalidKeySpecException) {
            throw RuntimeException("Invalid key format")
        }
    }
}
