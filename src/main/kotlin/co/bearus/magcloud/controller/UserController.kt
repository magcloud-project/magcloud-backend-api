package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.controller.dto.request.AuthRegisterDTO
import co.bearus.magcloud.controller.dto.request.UpdateRequestDTO
import co.bearus.magcloud.controller.dto.response.APIResponse
import co.bearus.magcloud.domain.service.diary.UserDiaryService
import co.bearus.magcloud.domain.service.user.UserService
import co.bearus.magcloud.domain.service.user.UserTagService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/v1/user")
class UserController(
    private val userService: UserService,
    private val userTagService: UserTagService,
    private val userDiaryService: UserDiaryService
) {
    @PostMapping
    fun onRegisterRequested(@RequestBody @Valid request: AuthRegisterDTO): ResponseEntity<APIResponse> {
        val result = userService.onRegisterRequest(request)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun onGetRequest(@RequestUser user: WebUser): ResponseEntity<co.bearus.magcloud.controller.dto.response.UserDTO> {
        val result = userService.getUserInfo(user.userId)
        return ResponseEntity.ok(result)
    }

    @PutMapping("/tag")
    fun onAdd(
        @RequestBody @Valid dto: co.bearus.magcloud.controller.dto.request.UserTagAddDTO,
        @RequestUser user: WebUser
    ): ResponseEntity<APIResponse> {
        val result = userTagService.addTagToUser(user.userId, dto.id)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/tag")
    fun onDelete(
        @RequestBody @Valid dto: co.bearus.magcloud.controller.dto.request.UserTagAddDTO,
        @RequestUser user: WebUser
    ): ResponseEntity<APIResponse> {
        val result = userTagService.deleteTagOfUser(user.userId, dto.id)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/tag")
    fun onGet(
        @RequestUser user: WebUser,
        token: String?
    ): ResponseEntity<List<co.bearus.magcloud.controller.dto.response.TagResponseDTO>> {
        val result = userTagService.getTagsOfUser(user.userId)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/diary")
    fun onDiaryAdd(
        @RequestBody @Valid dto: co.bearus.magcloud.controller.dto.request.DiaryCreateDTO,
        @RequestUser user: WebUser
    ): ResponseEntity<APIResponse> {
        val result = userDiaryService.addDiary(user.userId, dto.date, dto.content)
        return ResponseEntity.ok(result)
    }

    @PatchMapping("/diary")
    fun onDiaryPatch(
        @RequestBody @Valid dto: co.bearus.magcloud.controller.dto.request.DiaryPatchDTO,
        @RequestUser user: WebUser
    ): ResponseEntity<APIResponse> {
        val result = userDiaryService.patchDiary(user.userId, dto)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/diary")
    fun onDiaryGet(
        @RequestUser user: WebUser,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd") date: LocalDate?
    ): ResponseEntity<co.bearus.magcloud.controller.dto.response.DiaryResponseDTO> {
        return if (date == null) {
            val result = userDiaryService.getDiaryByDate(user.userId, LocalDate.now())
            ResponseEntity.ok(result)
        } else {
            val result = userDiaryService.getDiaryByDate(user.userId, date)
            ResponseEntity.ok(result)
        }
    }

    @GetMapping("/diaries")
    fun onDiaryAllGet(
        @RequestUser user: WebUser
    ): ResponseEntity<List<co.bearus.magcloud.controller.dto.response.DiaryResponseDTO>> {
        return ResponseEntity.ok(userDiaryService.getDiariesOfUser(user.userId))
    }

    @PostMapping("/diary-update")
    fun onDiaryUpdateRequest(
        @RequestUser user: WebUser,
        @RequestBody payload: List<UpdateRequestDTO>
    ): ResponseEntity<List<co.bearus.magcloud.controller.dto.response.DiaryResponseDTO>> {
        return ResponseEntity.ok(userDiaryService.updateRequest(user.userId, payload))
    }
}
