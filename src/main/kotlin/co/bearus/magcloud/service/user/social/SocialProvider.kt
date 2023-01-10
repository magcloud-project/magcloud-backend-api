package co.bearus.magcloud.service.user.social

import co.bearus.magcloud.dto.request.SocialLoginDTO
import co.bearus.magcloud.dto.response.LoginResponseDTO

interface SocialProvider {
    fun login(dto: SocialLoginDTO): LoginResponseDTO
}
