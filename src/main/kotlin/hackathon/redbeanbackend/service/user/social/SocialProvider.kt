package hackathon.redbeanbackend.service.user.social

import hackathon.redbeanbackend.dto.response.LoginResponseDTO

interface SocialProvider {
    fun login(authToken: String): LoginResponseDTO
}
