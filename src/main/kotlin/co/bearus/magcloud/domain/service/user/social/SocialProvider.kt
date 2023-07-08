package co.bearus.magcloud.domain.service.user.social

interface SocialProvider {
    fun login(dto: co.bearus.magcloud.controller.dto.request.SocialLoginDTO): co.bearus.magcloud.controller.dto.response.LoginResponseDTO
}
