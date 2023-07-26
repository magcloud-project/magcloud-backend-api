package co.bearus.magcloud.controller.dto.response

import co.bearus.magcloud.controller.dto.ResponseMessage
import co.bearus.magcloud.domain.type.ContextLanguage
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class APIResponse(
    val success: Boolean,
    val message: String
) {
    companion object {
        fun ok(message: String) = APIResponse(true, message)
        fun error(message: String) = APIResponse(false, message)
        fun ok(language: ContextLanguage, message: ResponseMessage) = APIResponse(true, message.message[language] ?: "")
        fun error(language: ContextLanguage, message: ResponseMessage) = APIResponse(false, message.message[language] ?: "")
    }
}
