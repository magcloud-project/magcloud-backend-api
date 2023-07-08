package co.bearus.magcloud.config.handler

import co.bearus.magcloud.controller.dto.response.APIResponse
import co.bearus.magcloud.domain.exception.DomainException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class WebAccessDeniedHandler(
    private val objectMapper: ObjectMapper,
) : AccessDeniedHandler {

    @Throws(IOException::class)
    private fun writeUnauthorized(response: HttpServletResponse) {
        response.status = HttpStatus.FORBIDDEN.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        response.outputStream.use { os ->
            objectMapper.writeValue(os, APIResponse.error("인증되지 않았습니다"))
            os.flush()
        }
    }

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        accessDeniedException: org.springframework.security.access.AccessDeniedException?
    ) {
        writeUnauthorized(response)
    }
}
