package co.bearus.magcloud.advice

import co.bearus.magcloud.domain.UnauthorizedException
import co.bearus.magcloud.service.user.TokenService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class RequestUserArgumentResolver(private val tokenService: TokenService): HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.getParameterAnnotation(RequestUser::class.java) != null
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val token = webRequest.getHeader("X-AUTH-TOKEN")
        val userId = tokenService.getIdFromToken(token ?: throw UnauthorizedException()) ?: throw UnauthorizedException()
        return WebUser(userId)
    }
}
