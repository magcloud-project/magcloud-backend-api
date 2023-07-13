package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.controller.dto.request.DiaryCreateDTO
import co.bearus.magcloud.controller.dto.request.DiaryPatchDTO
import co.bearus.magcloud.controller.dto.response.DiaryIntegrityResponseDTO
import co.bearus.magcloud.controller.dto.response.DiaryResponseDTO
import co.bearus.magcloud.controller.dto.response.FeedDTO
import co.bearus.magcloud.domain.exception.DiaryNotFoundException
import co.bearus.magcloud.domain.exception.DiaryTooOldException
import co.bearus.magcloud.domain.exception.UnAuthorizedException
import co.bearus.magcloud.domain.repository.QUserFeedRepository
import co.bearus.magcloud.domain.service.diary.UserDiaryService
import co.bearus.magcloud.domain.service.notification.NotificationService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/v1/diaries")
class DiaryController(
    private val userDiaryService: UserDiaryService,
    private val notificationService: NotificationService,
    private val feedRepository: QUserFeedRepository,
) {
    @PostMapping
    fun createDiary(
        @RequestBody @Valid dto: DiaryCreateDTO,
        @RequestUser user: WebUser,
    ): ResponseEntity<DiaryResponseDTO> {
        val date = LocalDate.now();
        val diaryDate = LocalDate.parse(dto.date, DateTimeFormatter.BASIC_ISO_DATE)
        val gap = date.toEpochDay() - diaryDate.toEpochDay()
        if (diaryDate.isAfter(date) || gap > 2) throw DiaryTooOldException()
        val result = userDiaryService.createDiary(
            userId = user.userId,
            diaryDate = dto.date,
            content = dto.content,
            emotion = dto.emotion,
            imageUrl = dto.imageUrl,
        )
        notificationService.sendDiaryCreateNotification(result)
        return ResponseEntity.ok(result)
    }

    @PatchMapping("/{diaryId}")
    fun updateDiary(
        @PathVariable diaryId: String,
        @RequestBody @Valid dto: DiaryPatchDTO,
        @RequestUser user: WebUser,
    ): ResponseEntity<DiaryResponseDTO> {
        val result = userDiaryService.updateDiary(diaryId, user.userId, dto)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/{diaryId}/like")
    fun likeDiary(
        @PathVariable diaryId: String,
        @RequestUser user: WebUser,
    ): ResponseEntity<FeedDTO> {
        userDiaryService.likeDiary(diaryId, user.userId)
        val feed = feedRepository.getFeedByDiaryId(diaryId, user.userId) ?: throw DiaryNotFoundException()
        return ResponseEntity.ok(feed.toDto())
    }

    @PostMapping("/{diaryId}/unlike")
    fun unlikeDiary(
        @PathVariable diaryId: String,
        @RequestUser user: WebUser,
    ): ResponseEntity<FeedDTO> {
        userDiaryService.unLikeDiary(diaryId, user.userId)
        val feed = feedRepository.getFeedByDiaryId(diaryId, user.userId) ?: throw DiaryNotFoundException()
        return ResponseEntity.ok(feed.toDto())
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
        val result = userDiaryService.getDiaryById(diaryId = diaryId)
        if (result.userId != user.userId) throw UnAuthorizedException()
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

    @GetMapping("/integrity", params = ["year", "month"])
    fun getMonthlyDiaryIntegrity(
        @RequestParam year: Int,
        @RequestParam month: Int,
        @RequestUser user: WebUser,
    ): ResponseEntity<List<DiaryIntegrityResponseDTO>> {
        val result = userDiaryService.getMonthlyIntegrityById(user.userId, year, month)
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
