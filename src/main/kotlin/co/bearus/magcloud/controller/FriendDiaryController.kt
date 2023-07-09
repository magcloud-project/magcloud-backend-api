package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.controller.dto.response.DiaryResponseDTO
import co.bearus.magcloud.domain.exception.DiaryNotExistsException
import co.bearus.magcloud.domain.exception.DiaryNotFoundException
import co.bearus.magcloud.domain.exception.IntegrityViolationException
import co.bearus.magcloud.domain.service.diary.UserDiaryService
import co.bearus.magcloud.domain.service.friend.FriendService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/v1/users/{userId}/diaries")
class FriendDiaryController(
    private val friendService: FriendService,
    private val diaryService: UserDiaryService,
) {
    @GetMapping(params = ["date"])
    fun getFriendDiaryByDate(
        @RequestUser user: WebUser,
        @PathVariable userId: String,
        @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") date: LocalDate,
    ): ResponseEntity<DiaryResponseDTO> {
        if (friendService.isFriend(user.userId, userId)) throw IntegrityViolationException()
        if (friendService.isDiaryReadable(user.userId, userId)) throw DiaryNotExistsException()
        val result = diaryService.getDiaryByDate(user.userId, date)
        return ResponseEntity.ok(result)
    }
}
