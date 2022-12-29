package hackathon.redbeanbackend.controller

import hackathon.redbeanbackend.domain.UnauthorizedException
import hackathon.redbeanbackend.dto.*
import hackathon.redbeanbackend.service.TokenService
import hackathon.redbeanbackend.service.UserDiaryService
import hackathon.redbeanbackend.service.UserService
import hackathon.redbeanbackend.service.UserTagService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
    ): ResponseEntity<List<DiaryResponseDTO>> {
        return if (date == null) {
            val result = userDiaryService.getDiariesOfUser(findUserByToken(token))
            ResponseEntity.ok(result)
        } else {
            val result = userDiaryService.getDiariesByDate(findUserByToken(token), date)
            ResponseEntity.ok(result)
        }
    }

    private fun findUserByToken(token: String?): Long {
        return tokenService.getIdFromToken(token ?: throw UnauthorizedException()) ?: throw UnauthorizedException()
    }
}
