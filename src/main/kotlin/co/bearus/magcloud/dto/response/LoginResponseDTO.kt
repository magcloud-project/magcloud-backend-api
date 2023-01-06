package co.bearus.magcloud.dto.response

data class LoginResponseDTO(
    val accessToken: String,
    val refreshToken: String
)
