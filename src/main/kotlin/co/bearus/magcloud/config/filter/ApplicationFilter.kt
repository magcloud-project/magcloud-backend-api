package co.bearus.magcloud.config.filter

import co.bearus.magcloud.controller.dto.response.ErrorResponse
import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.exception.ErrorCode
import co.bearus.magcloud.domain.service.AppInfoService
import co.bearus.magcloud.domain.type.ContextLanguage
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class ApplicationFilter(
    private val objectMapper: ObjectMapper,
    private val appInfoService: AppInfoService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val appLanguage = request.getHeader("X-APP-LANGUAGE") ?: "KOR"
        val contextLanguage = ContextLanguage.valueOf(appLanguage)
        request.setAttribute("language", contextLanguage)

        val appKey = request.getHeader("X-APP-KEY") ?: ""
        val appVersion = request.getHeader("X-APP-VERSION") ?: ""
        val appInfo = appInfoService.getAppInfoByKey(appKey)
        if (appInfo == null || appInfo.appVersion != appVersion || !appInfo.inService) {
            writeUpdateResponse(response, contextLanguage)
            return
        }

        filterChain.doFilter(request, response)
    }

    @Throws(IOException::class)
    private fun writeUpdateResponse(response: HttpServletResponse, language: ContextLanguage) {
        response.status = HttpStatus.FORBIDDEN.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        response.outputStream.use { os ->
            objectMapper.writeValue(os, ErrorResponse(
                    code = ErrorCode.APP_VERSION_EXCEPTION.code,
                    message = ErrorCode.APP_VERSION_EXCEPTION.message[language] ?: "",
                ))
            os.flush()
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath
        return path == "/health-check" || path.startsWith("/actuator/") || path.startsWith("/v1/auth")
    }
}
