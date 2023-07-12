package co.bearus.magcloud.domain.type

enum class NotificationType(
    val displayName: String,
) {
    SOCIAL("매지구름 소셜 알림"),
    APPLICATION("매지구름"),
    FEED("매지구름"),
}
