package co.bearus.magcloud.advice

import co.bearus.magcloud.config.filter.APIKeyAuthentication
import co.bearus.magcloud.domain.exception.UnAuthenticatedException
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class RequestUserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.getParameterAnnotation(RequestUser::class.java) != null
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val authorization = SecurityContextHolder.getContext().authentication
        if (authorization !is APIKeyAuthentication) throw UnAuthenticatedException()
        return WebUser(authorization.name)
    }
}
