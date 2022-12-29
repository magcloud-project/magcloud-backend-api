package hackathon.redbeanbackend.dto

data class PapagoRequestDTO(val source: String = "ko", val target: String = "en", val text: String) {
}
