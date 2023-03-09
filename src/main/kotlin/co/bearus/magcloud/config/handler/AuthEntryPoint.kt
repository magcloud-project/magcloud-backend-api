package co.bearus.magcloud.config.handler

import co.bearus.magcloud.controller.dto.response.APIResponse
import co.bearus.magcloud.domain.exception.DomainException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class AuthEntryPoint(
    private val objectMapper: ObjectMapper,
) : AuthenticationEntryPoint {

    @Throws(IOException::class)
    private fun writeUnauthorized(response: HttpServletResponse) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        response.outputStream.use { os ->
            objectMapper.writeValue(os, APIResponse.error("인증 정보가 올바르지 않습니다"))
            os.flush()
        }
    }

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authException: AuthenticationException?
    ) {
        writeUnauthorized(response)
    }
}
