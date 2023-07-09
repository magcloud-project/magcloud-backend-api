package co.bearus.magcloud.domain.service.diary

import co.bearus.magcloud.advice.SHA256
import co.bearus.magcloud.controller.dto.request.DiaryPatchDTO
import co.bearus.magcloud.controller.dto.response.APIResponse
import co.bearus.magcloud.controller.dto.response.DiaryIntegrityResponseDTO
import co.bearus.magcloud.controller.dto.response.DiaryResponseDTO
import co.bearus.magcloud.domain.entity.diary.DiaryEntity
import co.bearus.magcloud.domain.entity.user.UserEntity
import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.exception.NotFoundException
import co.bearus.magcloud.domain.repository.JPAUserDiaryRepository
import co.bearus.magcloud.domain.repository.JPAUserRepository
import co.bearus.magcloud.domain.repository.QUserDiaryRepository
import co.bearus.magcloud.domain.type.Emotion
import co.bearus.magcloud.util.DateUtils.Companion.toEpochMillis
import co.bearus.magcloud.util.ULIDUtils
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class UserDiaryService(
    private val userRepository: JPAUserRepository,
    private val diaryRepository: JPAUserDiaryRepository,
    private val qDiaryRepository: QUserDiaryRepository,
) {
    @Transactional
    fun createDiary(
        userId: String,
        diaryDate: String,
        content: String,
        emotion: Emotion,
    ): APIResponse {
        val user = findUser(userId)

        val date = LocalDate.parse(diaryDate, DateTimeFormatter.BASIC_ISO_DATE)
        val previousDiaries = diaryRepository.getByUserIdAndDate(userId, date)
        if (previousDiaries != null) throw DomainException("이미 해당 날짜에 일기가 존재합니다")

        val userDiary = DiaryEntity.createNewDiary(
            diaryId = ULIDUtils.generate(),
            userId = user.userId,
            emotion = emotion.name,
            date = date,
            content = content,
            contentHash = SHA256.encrypt(content),
        )
        diaryRepository.save(userDiary)
        return APIResponse.ok("일기가 추가되었습니다")
    }

    @Transactional
    fun updateDiary(
        diaryId: String,
        userId: String,
        dto: DiaryPatchDTO
    ): APIResponse {
        val previousDiary = diaryRepository
            .findById(diaryId)
            .orElseThrow { DomainException("일기가 존재하지 않습니다") }

        if (previousDiary.userId != userId) throw DomainException(" ???? ")

        previousDiary.updateDiary(dto)
        diaryRepository.save(previousDiary)
        return APIResponse.ok("일기가 수정되었습니다.")
    }

    fun getDiaryByDate(userId: String, date: LocalDate): DiaryResponseDTO? {
        val diary = diaryRepository.getByUserIdAndDate(userId, date)
            ?: throw NotFoundException()
        return diary.toDto()
    }

    fun getDiaryById(userId: String, diaryId: String): DiaryResponseDTO? {
        val diary = diaryRepository.findById(diaryId)
            .orElseThrow { NotFoundException() }
        return diary.toDto()
    }

    fun getDiaryIntegrityById(diaryId: String): DiaryIntegrityResponseDTO {
        val diary = qDiaryRepository.getDiaryIntegrityProjection(diaryId)
            ?: throw NotFoundException()
        return DiaryIntegrityResponseDTO(
            diaryId = diary.diaryId,
            contentHash = diary.contentHash,
            createdAtTs = diary.createdAt.toEpochMillis(),
            updatedAtTs = diary.updatedAt.toEpochMillis(),
        )
    }

    private fun findUser(userId: String): UserEntity {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        return user.get()
    }
}

