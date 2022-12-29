package hackathon.redbeanbackend.controller

import hackathon.redbeanbackend.dto.APIResponse
import hackathon.redbeanbackend.dto.AuthRegisterDTO
import hackathon.redbeanbackend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {
    @PostMapping
    fun onRegisterRequested(@RequestBody request: AuthRegisterDTO): ResponseEntity<APIResponse> {
        val result = userService.onRegisterRequest(request)
        return ResponseEntity.ok(result)
    }

}
