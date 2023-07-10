package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.controller.dto.request.DeviceRegisterDTO
import co.bearus.magcloud.controller.dto.response.APIResponse
import co.bearus.magcloud.domain.service.notification.NotificationService
import co.bearus.magcloud.domain.service.notification.UserDeviceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users/device")
class UserDeviceController(
    private val userDeviceService: UserDeviceService,
    private val notificationService: NotificationService,
) {
    @PostMapping
    fun registerNewDevice(
        @RequestUser user: WebUser,
        @RequestBody deviceRegisterDTO: DeviceRegisterDTO,
    ): ResponseEntity<APIResponse> {
        this.userDeviceService.registerDevice(user.userId, deviceRegisterDTO)
        return ResponseEntity.ok(APIResponse.ok("등록 성공"))
    }

    @DeleteMapping
    fun unregisterDevice(
        @RequestUser user: WebUser,
        @RequestBody deviceRegisterDTO: DeviceRegisterDTO,
    ): ResponseEntity<APIResponse> {
        this.userDeviceService.registerDevice(user.userId, deviceRegisterDTO)
        return ResponseEntity.ok(APIResponse.ok("삭제 성공"))
    }

    @PostMapping("/noti")
    fun sendNoti() {
        notificationService.broadcastMessage("asdf", "bsdef")
    }
}
