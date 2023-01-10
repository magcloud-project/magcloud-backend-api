package co.bearus.magcloud.service.user

import co.bearus.magcloud.dto.response.LoginResponseDTO
import co.bearus.magcloud.entity.user.UserEntity
import co.bearus.magcloud.entity.user.UserTokenEntity
import co.bearus.magcloud.repository.JPAUserTokenRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Service
class TokenService(
    val tokenRepository: JPAUserTokenRepository,
    @Value("\${token.secret}") val secret: String,
    @Value("\${token.expiration.access}") val accessTokenExpiration: Long,
    @Value("\${token.expiration.refresh}") val refreshTokenExpiration: Long,
) {
    lateinit var signKey: Key

    init {
        val bytes = secret.toByteArray()
        signKey = SecretKeySpec(bytes, SignatureAlgorithm.HS256.jcaName)
    }

    fun createRefreshToken(user: UserEntity): String {
        return Jwts.builder()
            .setHeader(buildHeader())
            .setClaims(user.toPayLoad())
            .setExpiration(generateRefreshTokenExpiration())
            .signWith(signKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun createAccessToken(user: UserEntity): String {
        return Jwts.builder()
            .setHeader(buildHeader())
            .setClaims(user.toPayLoad())
            .setExpiration(generateAccessTokenExpiration())
            .signWith(signKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun createToken(user: UserEntity): LoginResponseDTO {
        val accessToken = createAccessToken(user)
        val refreshToken = createRefreshToken(user)
        val tokenEntity =
            user.token?.apply { this.refreshToken = refreshToken } ?: UserTokenEntity(user.id, null, refreshToken)
        this.tokenRepository.save(tokenEntity)
        return LoginResponseDTO(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    private fun UserEntity.toPayLoad() =
        mapOf<String, Any>(
            Pair("id", this.id!!)
        )

    private fun buildHeader() =
        mapOf<String, Any>(
            Pair("typ", "JWT"),
            Pair("alg", "HS256"),
            Pair("regDate", System.currentTimeMillis())
        )

    private fun generateAccessTokenExpiration() = Date(System.currentTimeMillis() + this.accessTokenExpiration * 1000)
    private fun generateRefreshTokenExpiration() = Date(System.currentTimeMillis() + this.refreshTokenExpiration * 1000)

    fun getIdFromToken(token: String): Long? {
        return try {
            (Jwts.parserBuilder().setSigningKey(signKey).build()
                .parseClaimsJws(token).body["id"] as Int).toLong()
        } catch (e: Exception) {
            null
        }
    }
}
