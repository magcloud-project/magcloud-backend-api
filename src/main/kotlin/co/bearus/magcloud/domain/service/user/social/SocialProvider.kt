package co.bearus.magcloud.domain.service.user.social

import co.bearus.magcloud.controller.dto.request.SocialLoginDTO
import co.bearus.magcloud.controller.dto.response.LoginResponseDTO

interface SocialProvider {
    fun login(dto: SocialLoginDTO): LoginResponseDTO
}
