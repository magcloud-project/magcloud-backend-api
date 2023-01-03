package hackathon.redbeanbackend.controller

import hackathon.redbeanbackend.domain.UnauthorizedException
import hackathon.redbeanbackend.dto.request.DeviceRegisterDTO
import hackathon.redbeanbackend.dto.response.APIResponse
import hackathon.redbeanbackend.service.notification.UserDeviceService
import hackathon.redbeanbackend.service.user.TokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user/device")
class UserDeviceController(private val tokenService: TokenService, private val userDeviceService: UserDeviceService) {
    @PostMapping
    fun registerNewDevice(
        @RequestHeader(value = "X-AUTH-TOKEN") token: String?,
        @RequestBody deviceRegisterDTO: DeviceRegisterDTO
    ): ResponseEntity<APIResponse> {
        val userId = findUserByToken(token)
        return ResponseEntity.ok(this.userDeviceService.registerDevice(userId, deviceRegisterDTO))
    }

    private fun findUserByToken(token: String?): Long {
        return tokenService.getIdFromToken(token ?: throw UnauthorizedException()) ?: throw UnauthorizedException()
    }
}
