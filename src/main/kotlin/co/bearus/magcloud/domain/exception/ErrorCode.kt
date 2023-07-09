package co.bearus.magcloud.domain.exception

import co.bearus.magcloud.domain.type.ContextLanguage

enum class ErrorCode(
    val code: String,
    val message: Map<ContextLanguage, String>,
) {
    UNKNOWN_EXCEPTION("CM0001", simpleMessage("알 수 없는 오류가 발생했습니다", "Unknown error")),
    VALIDATION_EXCEPTION("CM0002", simpleMessage("입력값이 올바르지 않습니다", "Invalid input")),
    UNAUTHORIZED("CM0003", simpleMessage("인증되지 않은 사용자입니다", "Unauthorized user")),
    UNAUTHENTICATED("CM0004", simpleMessage("인증되지 않은 사용자입니다", "Unauthorized user")),
    INTEGRITY_VIOLATION("CM0005", simpleMessage("데이터가 올바르지 않습니다", "Invalid data")),
    TOKEN_EXPIRED("CM0006", simpleMessage("토큰이 만료되었습니다", "Token expired")),
    AUTH_FAILED("CM0007", simpleMessage("인증에 실패했습니다", "Authentication failed")),
    GENERIC_NOT_FOUND("CM0008", simpleMessage("찾을 수 없습니다", "Not found")),

    USER_NOT_FOUND("UE0001", simpleMessage("사용자를 찾을 수 없습니다", "User not found")),

    DIARY_NOT_FOUND("DE0001", simpleMessage("다이어리를 찾을 수 없습니다", "Diary not found")),

    ALREADY_FRIEND("FE0001", simpleMessage("이미 친구입니다", "Already friend")),
    ALREADY_FRIEND_REQUESTED("FE0002", simpleMessage("이미 친구 요청을 보냈습니다", "Already friend requested")),
    FRIEND_REQUEST_NOT_FOUND("FE0003", simpleMessage("친구 요청을 받지 않았습니다", "Friend request not received")),
}

private fun simpleMessage(kor: String, eng: String) = mapOf(
    ContextLanguage.KOR to kor,
    ContextLanguage.ENG to eng,
)
