package co.bearus.magcloud.controller.dto.response

data class LoginResponseDTO(
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean,
)
