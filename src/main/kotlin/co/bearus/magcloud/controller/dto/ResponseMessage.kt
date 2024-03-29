package co.bearus.magcloud.controller.dto

import co.bearus.magcloud.domain.type.ContextLanguage

enum class ResponseMessage(
    val message: Map<ContextLanguage, String>,
) {
    SENT_FRIEND_REQUEST(simpleMessage("친구 요청을 보냈습니다", "Sent a friend request")),
    ACCEPTED_FRIEND_REQUEST(simpleMessage("친구 요청을 수락했습니다", "Accepted a friend request")),
    DENIED_FRIEND_REQUEST(simpleMessage("친구 요청을 거절했습니다", "Denied a friend request")),
    BROKE_FRIEND(simpleMessage("친구 관계를 해제했습니다", "Broke a friend")),
    CANCELLED_FRIEND_REQUEST(simpleMessage("친구 요청을 취소했습니다", "Cancelled a friend request")),
    SHARED_DIARY(simpleMessage("다이어리를 공유했습니다", "Shared a diary")),
    HIDE_DIARY(simpleMessage("다이어리를 비공개했습니다", "Hid a diary")),
    LEAVED_MAGCLOUD(simpleMessage("매지구름을 탈퇴했습니다", "Leaved MagCloud")),
    NO_PERMISSION(simpleMessage("권한이 없습니다", "No permission")),
    SUCCESSFULLY_DELETED(simpleMessage("성공적으로 삭제되었습니다", "Successfully deleted")),
}

private fun simpleMessage(kor: String, eng: String) = mapOf(
    ContextLanguage.KOR to kor,
    ContextLanguage.ENG to eng,
)
