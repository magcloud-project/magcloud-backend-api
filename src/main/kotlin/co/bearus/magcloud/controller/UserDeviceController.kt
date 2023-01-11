package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.domain.UnauthorizedException
import co.bearus.magcloud.dto.request.DeviceRegisterDTO
import co.bearus.magcloud.dto.response.APIResponse
import co.bearus.magcloud.service.notification.UserDeviceService
import co.bearus.magcloud.service.user.TokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user/device")
class UserDeviceController(private val userDeviceService: UserDeviceService) {
    @PostMapping
    fun registerNewDevice(
        @RequestUser user: WebUser,
        @RequestBody deviceRegisterDTO: DeviceRegisterDTO
    ): ResponseEntity<APIResponse> {
        return ResponseEntity.ok(this.userDeviceService.registerDevice(user.userId, deviceRegisterDTO))
    }
}
