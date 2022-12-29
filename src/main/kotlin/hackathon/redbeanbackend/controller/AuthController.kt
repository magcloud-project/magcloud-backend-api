package hackathon.redbeanbackend.controller

import hackathon.redbeanbackend.dto.LoginDTO
import hackathon.redbeanbackend.dto.LoginResponseDTO
import hackathon.redbeanbackend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val userService: UserService) {
    @PostMapping
    fun requestLogin(@RequestBody dto: LoginDTO): ResponseEntity<LoginResponseDTO>{
        return ResponseEntity.ok(userService.onLoginRequest(dto))
    }
}
