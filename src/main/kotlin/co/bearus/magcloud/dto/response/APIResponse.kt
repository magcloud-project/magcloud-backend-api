package co.bearus.magcloud.dto.response

class APIResponse(val success: Boolean, val message: String) {
    companion object {
        fun ok(message: String) = APIResponse(true, message)
        fun error(message: String) = APIResponse(false, message)
    }
}
