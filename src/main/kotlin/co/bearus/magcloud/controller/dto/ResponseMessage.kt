package co.bearus.magcloud.controller.dto

import co.bearus.magcloud.domain.type.ContextLanguage

enum class ResponseMessage(
    val message: Map<ContextLanguage, String>,
) {
    SENT_FRIEND_REQUEST(simpleMessage("친구 요청을 보냈습니다", "Sent a friend request")),
    ACCEPTED_FRIEND_REQUEST(simpleMessage("친구 요청을 수락했습니다", "Accepted a friend request")),
    DENIED_FRIEND_REQUEST(simpleMessage("친구 요청을 거절했습니다", "Denied a friend request")),
    BROKE_FRIEND(simpleMessage("친구 관계를 해제했습니다", "Broke a friend")),
}

private fun simpleMessage(kor: String, eng: String) = mapOf(
    ContextLanguage.KOR to kor,
    ContextLanguage.ENG to eng,
)
