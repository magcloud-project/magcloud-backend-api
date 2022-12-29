package hackathon.redbeanbackend.service

import hackathon.redbeanbackend.domain.DomainException
import hackathon.redbeanbackend.dto.APIResponse
import hackathon.redbeanbackend.dto.AuthRegisterDTO
import hackathon.redbeanbackend.entity.UserEntity
import hackathon.redbeanbackend.repository.JPAUserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Service
class TokenService(@Value("\${secret.token}") val secret: String) {
    lateinit var signKey: Key
    init {
        val bytes = secret.toByteArray()
        signKey = SecretKeySpec(bytes, SignatureAlgorithm.HS256.jcaName)
    }
    fun createToken(user: UserEntity): String {
        return Jwts.builder()
            .setHeader(buildHeader())
            .setClaims(user.toPayLoad())
            .setExpiration(generateAccessTokenExpiration())
            .signWith(signKey, SignatureAlgorithm.HS256)
            .compact()
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
    private fun generateAccessTokenExpiration() = Date(System.currentTimeMillis() + 9999999 * 1000)
}
