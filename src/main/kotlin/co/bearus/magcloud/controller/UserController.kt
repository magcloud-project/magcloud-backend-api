package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.dto.request.*
import co.bearus.magcloud.dto.response.APIResponse
import co.bearus.magcloud.dto.response.DiaryResponseDTO
import co.bearus.magcloud.dto.response.TagResponseDTO
import co.bearus.magcloud.dto.response.UserDTO
import co.bearus.magcloud.service.diary.UserDiaryService
import co.bearus.magcloud.service.user.UserService
import co.bearus.magcloud.service.user.UserTagService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/user")
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
    fun onGetRequest(@RequestUser user: WebUser): ResponseEntity<UserDTO> {
        val result = userService.getUserInfo(user.userId)
        return ResponseEntity.ok(result)
    }

    @PutMapping("/tag")
    fun onAdd(
        @RequestBody @Valid dto: UserTagAddDTO,
        @RequestUser user: WebUser
    ): ResponseEntity<APIResponse> {
        val result = userTagService.addTagToUser(user.userId, dto.id!!)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/tag")
    fun onDelete(
        @RequestBody @Valid dto: UserTagAddDTO,
        @RequestUser user: WebUser
    ): ResponseEntity<APIResponse> {
        val result = userTagService.deleteTagOfUser(user.userId, dto.id!!)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/tag")
    fun onGet(@RequestUser user: WebUser, token: String?): ResponseEntity<List<TagResponseDTO>> {
        val result = userTagService.getTagsOfUser(user.userId)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/diary")
    fun onDiaryAdd(
        @RequestBody @Valid dto: DiaryCreateDTO,
        @RequestUser user: WebUser
    ): ResponseEntity<APIResponse> {
        val result = userDiaryService.addDiary(user.userId, dto.date!!, dto.content!!)
        return ResponseEntity.ok(result)
    }

    @PatchMapping("/diary")
    fun onDiaryPatch(
        @RequestBody @Valid dto: DiaryPatchDTO,
        @RequestUser user: WebUser
    ): ResponseEntity<APIResponse> {
        val result = userDiaryService.patchDiary(user.userId, dto)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/diary")
    fun onDiaryGet(
        @RequestUser user: WebUser,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd") date: LocalDate?
    ): ResponseEntity<DiaryResponseDTO> {
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
    ): ResponseEntity<List<DiaryResponseDTO>> {
        return ResponseEntity.ok(userDiaryService.getDiariesOfUser(user.userId))
    }

    @PostMapping("/diary-update")
    fun onDiaryUpdateRequest(
        @RequestUser user: WebUser,
        @RequestBody payload: List<UpdateRequestDTO>
    ): ResponseEntity<List<DiaryResponseDTO>> {
        return ResponseEntity.ok(userDiaryService.updateRequest(user.userId, payload))
    }
}
