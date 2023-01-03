package hackathon.redbeanbackend.controller

import hackathon.redbeanbackend.dto.request.LoginDTO
import hackathon.redbeanbackend.dto.response.LoginResponseDTO
import hackathon.redbeanbackend.service.user.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val userService: UserService) {
    @PostMapping
    fun requestLogin(@RequestBody @Valid dto: LoginDTO): ResponseEntity<LoginResponseDTO> {
        return ResponseEntity.ok(userService.onLoginRequest(dto))
    }
}
