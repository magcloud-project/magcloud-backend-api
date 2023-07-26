package co.bearus.magcloud.controller

import co.bearus.magcloud.advice.RequestLanguage
import co.bearus.magcloud.advice.RequestUser
import co.bearus.magcloud.advice.WebUser
import co.bearus.magcloud.controller.dto.ResponseMessage
import co.bearus.magcloud.controller.dto.request.DiaryCommentCreateDTO
import co.bearus.magcloud.controller.dto.request.DiaryCommentDTO
import co.bearus.magcloud.controller.dto.response.APIResponse
import co.bearus.magcloud.domain.entity.diary.DiaryCommentEntity
import co.bearus.magcloud.domain.repository.JPADiaryCommentRepository
import co.bearus.magcloud.domain.repository.QUserDiaryRepository
import co.bearus.magcloud.domain.service.diary.UserDiaryService
import co.bearus.magcloud.domain.service.notification.NotificationService
import co.bearus.magcloud.domain.type.ContextLanguage
import co.bearus.magcloud.util.ULIDUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/diaries/{diaryId}/comments")
class DiaryCommentController(
    private val diaryCommentRepository: JPADiaryCommentRepository,
    private val diaryService: UserDiaryService,
    private val qUserDiaryRepository: QUserDiaryRepository,
    private val notificationService: NotificationService,
) {
    @Transactional
    @PostMapping
    fun createComment(
        @RequestUser user: WebUser,
        @PathVariable diaryId: String,
        @RequestBody body: DiaryCommentCreateDTO,
    ): DiaryCommentDTO {
        val diary = diaryService.getDiaryById(diaryId)
        val comment = DiaryCommentEntity.createNewComment(
            commentId = ULIDUtils.generate(),
            diaryId = diary.diaryId,
            userId = user.userId,
            content = body.content,
        )
        val result = diaryCommentRepository.save(comment).toDto()
        notificationService.sendCommentCreateNotification(result)
        return result
    }

    @GetMapping
    fun getComments(
        @PathVariable diaryId: String,
    ): List<DiaryCommentDTO> {
        return qUserDiaryRepository.getDiaryComments(diaryId).map { it.toDto() }
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @RequestUser user: WebUser,
        @RequestLanguage language: ContextLanguage,
        @PathVariable diaryId: String,
        @PathVariable commentId: String,
    ): APIResponse {
        val result = diaryCommentRepository.findByIdOrNull(commentId);
        if (result == null || result.userId != user.userId) {
            return APIResponse.error(language, ResponseMessage.NO_PERMISSION);
        }
        diaryCommentRepository.delete(result)
        return APIResponse.ok(language, ResponseMessage.SUCCESSFULLY_DELETED);
    }
}
