package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.domain.service.notification.NotificationService
import co.bearus.magcloud.domain.service.notification.UserDeviceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/user/device")
class UserDeviceController(
    private val userDeviceService: UserDeviceService,
    private val notificationService: NotificationService
) {
    @PostMapping
    fun registerNewDevice(
        @RequestUser user: WebUser,
        @RequestBody deviceRegisterDTO: co.bearus.magcloud.controller.dto.request.DeviceRegisterDTO
    ): ResponseEntity<co.bearus.magcloud.controller.dto.response.APIResponse> {
        return ResponseEntity.ok(this.userDeviceService.registerDevice(user.userId, deviceRegisterDTO))
    }

    @PostMapping("/noti")
    fun sendNoti() {
        notificationService.broadcastMessage("asdf", "bsdef")
    }
}
