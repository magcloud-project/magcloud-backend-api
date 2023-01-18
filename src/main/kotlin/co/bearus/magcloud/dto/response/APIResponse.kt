package co.bearus.magcloud.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class APIResponse(val success: Boolean, val message: String, val data: Any? = null) {
    companion object {
        fun ok(message: String) = APIResponse(true, message)
        fun ok(message: String, data: Any?) = APIResponse(true, message, data)
        fun error(message: String) = APIResponse(false, message)
        fun error(message: String, data: Any?) = APIResponse(false, message, data)
    }
}
