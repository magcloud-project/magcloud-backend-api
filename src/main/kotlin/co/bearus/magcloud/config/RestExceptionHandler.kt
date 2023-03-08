package co.bearus.magcloud.config

import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.exception.NotFoundException
import co.bearus.magcloud.domain.exception.UnauthorizedException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler
    fun userExHandle(e: NotFoundException): ResponseEntity<co.bearus.magcloud.controller.dto.response.APIResponse>? {
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler
    fun validationException(e: MethodArgumentNotValidException): ResponseEntity<co.bearus.magcloud.controller.dto.response.APIResponse>? {
        return ResponseEntity.badRequest()
            .body(
                co.bearus.magcloud.controller.dto.response.APIResponse.error(
                    e.fieldErrors.firstOrNull()?.defaultMessage ?: "알 수 없는 오류입니다"
                )
            )
    }

    @ExceptionHandler
    fun unauthorized(e: UnauthorizedException): ResponseEntity<co.bearus.magcloud.controller.dto.response.APIResponse>? {
        return ResponseEntity.status(401)
            .body(co.bearus.magcloud.controller.dto.response.APIResponse.error("토큰이 만료되었거나 사용할 수 없습니다"))
    }

    @ExceptionHandler
    fun userExHandle(e: DomainException): ResponseEntity<co.bearus.magcloud.controller.dto.response.APIResponse>? {
        return ResponseEntity.badRequest()
            .body(co.bearus.magcloud.controller.dto.response.APIResponse.error(e.message!!))
    }

    @ExceptionHandler
    fun defaultHandle(e: RuntimeException): ResponseEntity<co.bearus.magcloud.controller.dto.response.APIResponse>? {
        e.printStackTrace()
        return ResponseEntity.badRequest()
            .body(co.bearus.magcloud.controller.dto.response.APIResponse.error("알 수 없는 오류가 발생했습니다"))
    }
}
