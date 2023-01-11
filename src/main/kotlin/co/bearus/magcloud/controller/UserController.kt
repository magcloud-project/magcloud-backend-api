package co.bearus.magcloud.controller

import co.bearus.magcloud.domain.UnauthorizedException
import co.bearus.magcloud.dto.request.AuthRegisterDTO
import co.bearus.magcloud.dto.request.DiaryCreateDTO
import co.bearus.magcloud.dto.request.UserTagAddDTO
import co.bearus.magcloud.dto.response.APIResponse
import co.bearus.magcloud.dto.response.DiaryResponseDTO
import co.bearus.magcloud.dto.response.TagResponseDTO
import co.bearus.magcloud.dto.response.UserDTO
import co.bearus.magcloud.service.diary.UserDiaryService
import co.bearus.magcloud.service.user.TokenService
import co.bearus.magcloud.service.user.UserService
import co.bearus.magcloud.service.user.UserTagService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
    private val userTagService: UserTagService,
    private val tokenService: TokenService,
    private val userDiaryService: UserDiaryService
) {
    @PostMapping
    fun onRegisterRequested(@RequestBody @Valid request: AuthRegisterDTO): ResponseEntity<APIResponse> {
        val result = userService.onRegisterRequest(request)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun onGetRequest(@RequestHeader(value = "X-AUTH-TOKEN") token: String?): ResponseEntity<UserDTO> {
        val result = userService.getUserInfo(findUserByToken(token))
        return ResponseEntity.ok(result)
    }

    @PutMapping("/tag")
    fun onAdd(
        @RequestBody @Valid dto: UserTagAddDTO,
        @RequestHeader(value = "X-AUTH-TOKEN") token: String?
    ): ResponseEntity<APIResponse> {
        val result = userTagService.addTagToUser(findUserByToken(token), dto.id!!)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/tag")
    fun onDelete(
        @RequestBody @Valid dto: UserTagAddDTO,
        @RequestHeader(value = "X-AUTH-TOKEN") token: String?
    ): ResponseEntity<APIResponse> {
        val result = userTagService.deleteTagOfUser(findUserByToken(token), dto.id!!)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/tag")
    fun onGet(@RequestHeader(value = "X-AUTH-TOKEN") token: String?): ResponseEntity<List<TagResponseDTO>> {
        val result = userTagService.getTagsOfUser(findUserByToken(token))
        return ResponseEntity.ok(result)
    }

    @PostMapping("/diary")
    fun onDiaryAdd(
        @RequestBody @Valid dto: DiaryCreateDTO,
        @RequestHeader(value = "X-AUTH-TOKEN") token: String?
    ): ResponseEntity<APIResponse> {
        val result = userDiaryService.addDiary(findUserByToken(token), dto.date!!, dto.content!!)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/diary")
    fun onDiaryGet(
        @RequestHeader(value = "X-AUTH-TOKEN") token: String?,
        @RequestParam(required = false) date: String?
    ): ResponseEntity<DiaryResponseDTO> {
        val user = findUserByToken(token)
        return if (date == null) {
            val result = userDiaryService.getDiaryByDate(user, getToday())
            ResponseEntity.ok(result)
        } else {
            val result = userDiaryService.getDiaryByDate(user, date)
            ResponseEntity.ok(result)
        }
    }

    @GetMapping("/diaries")
    fun onDiaryAllGet(
        @RequestHeader(value = "X-AUTH-TOKEN") token: String?,
    ): ResponseEntity<List<DiaryResponseDTO>> {
        val user = findUserByToken(token)
        return ResponseEntity.ok(userDiaryService.getDiariesOfUser(user))
    }

    private fun findUserByToken(token: String?): Long {
        return tokenService.getIdFromToken(token ?: throw UnauthorizedException()) ?: throw UnauthorizedException()
    }

    private fun getToday(): String {
        val date = LocalDate.now()
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    }
}
