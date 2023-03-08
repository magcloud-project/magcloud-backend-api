package co.bearus.magcloud.domain.service.user.social

import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.type.LoginProvider
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class KakaoProviderService(
    private val socialService: SocialService,
    @Value("\${secret.kakao-client-id}") val kakaoClientId: String,
    @Value("\${secret.kakao-redirect-url}") val kakaoRedirectUrl: String,
) : SocialProvider {
    override fun login(dto: co.bearus.magcloud.controller.dto.request.SocialLoginDTO): co.bearus.magcloud.controller.dto.response.LoginResponseDTO {
        try {
            val accessToken = getAccessTokenByCode(dto.accessToken)
            val socialLoginDto = getUserInfoByAccessToken(accessToken)
            return socialService.socialLogin(LoginProvider.KAKAO, socialLoginDto)
        } catch (e: Exception) {
            e.printStackTrace()
            throw DomainException()
        }
    }

    fun getAccessTokenByCode(code: String): String {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val params: LinkedMultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("grant_type", "authorization_code")
        params.add("client_id", this.kakaoClientId)
        params.add("redirect_uri", this.kakaoRedirectUrl)
        params.add("code", code)
        params.add("client_secret", "")
        val request: HttpEntity<MultiValueMap<String, String>> = HttpEntity(params, headers)
        val url = "https://kauth.kakao.com/oauth/token"
        val response: ResponseEntity<String> = restTemplate.postForEntity(url, request, String::class.java)
        val responseBody = response.body
        val objectMapper = ObjectMapper()
        val jsonNode: JsonNode = objectMapper.readTree(responseBody)
        return jsonNode.get("access_token").asText()
    }

    data class KakaoUserResponse(val id: Long, val connected_at: String, val kakao_account: KakaoEmailAccount?)
    data class KakaoEmailAccount(
        val email: String?,
        val email_needs_agreement: Boolean?,
        val is_email_valid: Boolean?,
        val is_email_verified: Boolean?
    )

    fun getUserInfoByAccessToken(accessToken: String): co.bearus.magcloud.controller.dto.SocialInfoDTO {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers["Authorization"] = "Bearer $accessToken"
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val params = LinkedMultiValueMap<String, String>()
        val request = HttpEntity<MultiValueMap<String, String>>(params, headers)
        val url = "https://kapi.kakao.com/v2/user/me"
        val dat = restTemplate.postForObject(url, request, String::class.java)
        val response = Gson().fromJson(dat, KakaoUserResponse::class.java)
        return co.bearus.magcloud.controller.dto.SocialInfoDTO(
            "kakao",
            response.id.toString(),
            response?.kakao_account?.email ?: "email-unavailable"
        )
    }
}
