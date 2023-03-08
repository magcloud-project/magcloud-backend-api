package co.bearus.magcloud.domain.service.user.social

import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.type.LoginProvider
import com.google.gson.Gson
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class KakaoNativeProviderService(
    private val socialService: SocialService
) : SocialProvider {
    override fun login(dto: co.bearus.magcloud.controller.dto.request.SocialLoginDTO): co.bearus.magcloud.controller.dto.response.LoginResponseDTO {
        try {
            val socialLoginDto = getUserInfoByAccessToken(dto)
            return socialService.socialLogin(LoginProvider.KAKAO, socialLoginDto)
        } catch (e: Exception) {
            e.printStackTrace()
            throw DomainException()
        }
    }

    data class KakaoUserResponse(val id: Long, val connected_at: String, val kakao_account: KakaoEmailAccount?)
    data class KakaoEmailAccount(
        val email: String?,
        val email_needs_agreement: Boolean?,
        val is_email_valid: Boolean?,
        val is_email_verified: Boolean?
    )

    fun getUserInfoByAccessToken(dto: co.bearus.magcloud.controller.dto.request.SocialLoginDTO): co.bearus.magcloud.controller.dto.SocialInfoDTO {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers["Authorization"] = "Bearer ${dto.accessToken}"
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val params = LinkedMultiValueMap<String, String>()
        val request = HttpEntity<MultiValueMap<String, String>>(params, headers)
        val url = "https://kapi.kakao.com/v2/user/me"
        val dat = restTemplate.postForObject(url, request, String::class.java)
        val response = Gson().fromJson(dat, KakaoUserResponse::class.java)
        return co.bearus.magcloud.controller.dto.SocialInfoDTO(
            dto.name ?: "kakao",
            response.id.toString(),
            response?.kakao_account?.email ?: "email-unavailable"
        )
    }
}
