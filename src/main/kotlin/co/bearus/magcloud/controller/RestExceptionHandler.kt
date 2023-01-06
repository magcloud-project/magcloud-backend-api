package co.bearus.magcloud.controller

import co.bearus.magcloud.domain.DomainException
import co.bearus.magcloud.domain.NotFoundException
import co.bearus.magcloud.domain.UnauthorizedException
import co.bearus.magcloud.dto.response.APIResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler
    fun userExHandle(e: NotFoundException): ResponseEntity<APIResponse>? {
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler
    fun validationException(e: MethodArgumentNotValidException): ResponseEntity<APIResponse>? {
        return ResponseEntity.badRequest()
            .body(APIResponse.error(e.fieldErrors.firstOrNull()?.defaultMessage ?: "알 수 없는 오류입니다"))
    }

    @ExceptionHandler
    fun unauthorized(e: UnauthorizedException): ResponseEntity<APIResponse>? {
        return ResponseEntity.status(401).body(APIResponse.error("토큰이 만료되었거나 사용할 수 없습니다"))
    }

    @ExceptionHandler
    fun userExHandle(e: DomainException): ResponseEntity<APIResponse>? {
        return ResponseEntity.badRequest().body(APIResponse.error(e.message!!))
    }

    @ExceptionHandler
    fun defaultHandle(e: RuntimeException): ResponseEntity<APIResponse>? {
        e.printStackTrace()
        return ResponseEntity.badRequest().body(APIResponse.error("알 수 없는 오류가 발생했습니다"))
    }
}
