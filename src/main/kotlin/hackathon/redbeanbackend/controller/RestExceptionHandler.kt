package hackathon.redbeanbackend.controller

import hackathon.redbeanbackend.domain.DomainException
import hackathon.redbeanbackend.dto.APIResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler
    fun userExHandle(e: DomainException): ResponseEntity<APIResponse>? {
        return ResponseEntity.badRequest().body(APIResponse.error(e.message!!))
    }
    @ExceptionHandler
    fun defaultHandle(e: RuntimeException): ResponseEntity<APIResponse>? {
        return ResponseEntity.badRequest().body(APIResponse.error("알 수 없는 오류가 발생했습니다"))
    }
}
