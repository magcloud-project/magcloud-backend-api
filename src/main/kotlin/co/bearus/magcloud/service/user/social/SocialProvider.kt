package co.bearus.magcloud.service.user.social

import co.bearus.magcloud.dto.response.LoginResponseDTO

interface SocialProvider {
    fun login(authToken: String): LoginResponseDTO
}
