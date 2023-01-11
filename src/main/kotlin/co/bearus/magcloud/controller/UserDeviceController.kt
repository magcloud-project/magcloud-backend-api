package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.dto.request.DeviceRegisterDTO
import co.bearus.magcloud.dto.response.APIResponse
import co.bearus.magcloud.service.notification.UserDeviceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
