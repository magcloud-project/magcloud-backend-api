package hackathon.redbeanbackend.dto.response

data class LoginResponseDTO(
    val accessToken: String,
    val refreshToken: String
)
