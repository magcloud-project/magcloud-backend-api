package co.bearus.magcloud.controller

import co.bearus.magcloud.domain.UnauthorizedException
import co.bearus.magcloud.dto.request.DeviceRegisterDTO
import co.bearus.magcloud.dto.response.APIResponse
import co.bearus.magcloud.service.notification.UserDeviceService
import co.bearus.magcloud.service.user.TokenService
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
