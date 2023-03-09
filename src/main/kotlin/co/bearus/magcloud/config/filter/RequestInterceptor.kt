package co.bearus.magcloud.config.filter

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class RequestInterceptor : HandlerInterceptor {

    companion object {
        const val START_TIME_ATTR_NAME = "startTime"
    }

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val startTime = System.currentTimeMillis()
        request.setAttribute(START_TIME_ATTR_NAME, startTime)
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        if(request.requestURI.equals("/api/health-check")) return
        val startTime = request.getAttribute(START_TIME_ATTR_NAME) as Long
        val endTime = System.currentTimeMillis()
        val origin = request.getHeader("X-Forwarded-For") ?: request.remoteAddr
        val executionTime = endTime - startTime

        log.info(
            "{} {} {} {} {}ms",
            request.method,
            request.requestURI,
            response.status,
            origin,
            executionTime,
        )
    }
}
