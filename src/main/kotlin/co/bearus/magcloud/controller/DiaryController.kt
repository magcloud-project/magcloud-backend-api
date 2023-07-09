package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.controller.dto.request.DiaryCreateDTO
import co.bearus.magcloud.controller.dto.request.DiaryPatchDTO
import co.bearus.magcloud.controller.dto.response.APIResponse
import co.bearus.magcloud.controller.dto.response.DiaryIntegrityResponseDTO
import co.bearus.magcloud.controller.dto.response.DiaryResponseDTO
import co.bearus.magcloud.domain.service.diary.UserDiaryService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/v1/diaries")
class DiaryController(
    private val userDiaryService: UserDiaryService,
) {
    @PostMapping
    fun createDiary(
        @RequestBody @Valid dto: DiaryCreateDTO,
        @RequestUser user: WebUser,
    ): ResponseEntity<APIResponse> {
        val result = userDiaryService.createDiary(
            userId = user.userId,
            diaryDate = dto.date,
            content = dto.content,
            emotion = dto.emotion,
        )
        return ResponseEntity.ok(result)
    }

    @PatchMapping("/{diaryId}")
    fun updateDiary(
        @PathVariable diaryId: String,
        @RequestBody @Valid dto: DiaryPatchDTO,
        @RequestUser user: WebUser,
    ): ResponseEntity<APIResponse> {
        val result = userDiaryService.updateDiary(diaryId, user.userId, dto)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun fetchTodayDiary(
        @RequestUser user: WebUser,
    ): ResponseEntity<DiaryResponseDTO> {
        val result = userDiaryService.getDiaryByDate(user.userId, LocalDate.now())
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{diaryId}")
    fun getDiary(
        @PathVariable diaryId: String,
        @RequestUser user: WebUser,
    ): ResponseEntity<DiaryResponseDTO> {
        val result = userDiaryService.getDiaryById(diaryId, user.userId)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{diaryId}/integrity")
    fun getDiaryIntegrity(
        @PathVariable diaryId: String,
        @RequestUser user: WebUser,
    ): ResponseEntity<DiaryIntegrityResponseDTO> {
        val result = userDiaryService.getDiaryIntegrityById(diaryId)
        return ResponseEntity.ok(result)
    }

    @GetMapping(params = ["date"])
    fun fetchDiary(
        @RequestUser user: WebUser,
        @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") date: LocalDate,
    ): ResponseEntity<DiaryResponseDTO> {
        val result = userDiaryService.getDiaryByDate(user.userId, date)
        return ResponseEntity.ok(result)
    }
}
