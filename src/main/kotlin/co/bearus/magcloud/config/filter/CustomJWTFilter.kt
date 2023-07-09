package co.bearus.magcloud.config.filter

import co.bearus.magcloud.provider.TokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomJWTFilter(
    @Value("\${app.headers.authToken}") private val authTokenHeaderName: String,
    private val tokenProvider: TokenProvider,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val accessToken = request.getHeader(authTokenHeaderName)
        val retrievedId = accessToken?.let { tokenProvider.getIdFromToken(it) }
        if (retrievedId != null) {
            val authentication = APIKeyAuthentication(
                userId = retrievedId,
                token = accessToken,
            )
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }
}
