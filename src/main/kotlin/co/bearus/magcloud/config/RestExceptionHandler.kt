package co.bearus.magcloud.config

import co.bearus.magcloud.controller.dto.response.ErrorResponse
import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.exception.ErrorCode
import co.bearus.magcloud.domain.exception.UnAuthenticatedException
import co.bearus.magcloud.domain.exception.UnAuthorizedException
import co.bearus.magcloud.domain.type.ContextLanguage
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler
    fun validationException(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .badRequest()
            .body(
                errorBody(ErrorCode.VALIDATION_EXCEPTION, request.getLanguage())
            )
    }

    @ExceptionHandler(value = [UnAuthenticatedException::class])
    fun handleUnAuthenticated(
        exception: UnAuthenticatedException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(401)
            .body(
                errorBody(exception.errorCode, request.getLanguage())
            )
    }

    @ExceptionHandler(value = [UnAuthorizedException::class])
    fun handleUnAuthorized(
        exception: UnAuthorizedException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(403)
            .body(
                errorBody(exception.errorCode, request.getLanguage())
            )
    }

    @ExceptionHandler(value = [DomainException::class])
    fun handleDomainException(
        exception: DomainException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .badRequest()
            .body(
                errorBody(exception.errorCode, request.getLanguage())
            )
    }

    @ExceptionHandler
    fun unhandledException(
        exception: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        exception.printStackTrace()
        return ResponseEntity
            .badRequest()
            .body(
                errorBody(ErrorCode.UNKNOWN_EXCEPTION, request.getLanguage())
            )
    }

    private fun errorBody(errorCode: ErrorCode, contextLanguage: ContextLanguage) = ErrorResponse(
        code = errorCode.code,
        message = errorCode.message[contextLanguage] ?: errorCode.code,
    )

    private fun HttpServletRequest.getLanguage() = this.getAttribute("language") as ContextLanguage
}
