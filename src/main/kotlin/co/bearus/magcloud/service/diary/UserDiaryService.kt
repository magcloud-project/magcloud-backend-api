package co.bearus.magcloud.service.diary

import co.bearus.magcloud.advice.SHA256
import co.bearus.magcloud.domain.DomainException
import co.bearus.magcloud.domain.NotFoundException
import co.bearus.magcloud.dto.request.DiaryPatchDTO
import co.bearus.magcloud.dto.request.UpdateRequestDTO
import co.bearus.magcloud.dto.response.APIResponse
import co.bearus.magcloud.dto.response.DiaryResponseDTO
import co.bearus.magcloud.dto.response.EmotionResponseDTO
import co.bearus.magcloud.entity.diary.UserDiaryEntity
import co.bearus.magcloud.entity.user.UserEntity
import co.bearus.magcloud.repository.JPAUserDiaryRepository
import co.bearus.magcloud.repository.JPAUserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class UserDiaryService(
    private val userRepository: JPAUserRepository,
    private val diaryRepository: JPAUserDiaryRepository,
    private val inferenceService: InferenceService
) {
    fun addDiary(userId: Long, diaryDate: String, content: String): APIResponse {
        val user = findUser(userId)

        val date = LocalDate.parse(diaryDate, DateTimeFormatter.BASIC_ISO_DATE)
        val previousDiaries = diaryRepository.getByUserIdAndDate(userId, date)
        if (previousDiaries != null) throw DomainException("이미 해당 날짜에 일기가 존재합니다")

        val userDiary = UserDiaryEntity(
            content,
            user,
            date
        )
        val requestedResult = diaryRepository.save(userDiary)
        inferenceService.requestInference(requestedResult)
        return APIResponse.ok("일기가 추가되었습니다")
    }

    fun patchDiary(userId: Long, dto: DiaryPatchDTO): APIResponse {
        val date = LocalDate.parse(dto.date, DateTimeFormatter.BASIC_ISO_DATE)
        val previousDiaries =
            diaryRepository.getByUserIdAndDate(userId, date) ?: throw DomainException("일기가 존재하지 않습니다")


        previousDiaries.content = dto.content
        previousDiaries.contentHash = SHA256.encrypt(dto.content)


        diaryRepository.save(previousDiaries)
        inferenceService.requestInference(previousDiaries)
        return APIResponse.ok("일기가 수정되었습니다.");
    }

    fun getDiariesOfUser(userId: Long): List<DiaryResponseDTO> {
        val user = findUser(userId)
        return user.diaries.map {
            DiaryResponseDTO(
                it.id!!,
                it.content,
                it.date.atStartOfDay(),
                it.modifiedDate!!,
                it.emotions.map { emotion -> EmotionResponseDTO(emotion.emotion, emotion.value) }
            )
        }
    }

    fun updateRequest(userId: Long, payload: List<UpdateRequestDTO>): List<DiaryResponseDTO> {
        val user = findUser(userId)
        val dataMap = payload.associate { it.date to it.contentHash }
        return user.diaries
            .filter { !dataMap.containsKey(it.date) || dataMap[it.date] != it.contentHash }
            .map {
                DiaryResponseDTO(
                    it.id!!,
                    it.content,
                    it.date.atStartOfDay(),
                    it.modifiedDate!!,
                    it.emotions.map { emotion -> EmotionResponseDTO(emotion.emotion, emotion.value) }
                )
            }
    }

    fun getDiaryByDate(userId: Long, date: LocalDate): DiaryResponseDTO? {
        val diary = diaryRepository.getByUserIdAndDate(userId, date)
            ?: throw NotFoundException()
        return DiaryResponseDTO(
            diary.id!!,
            diary.content,
            diary.date.atStartOfDay(),
            diary.modifiedDate!!,
            diary.emotions.map { emotion -> EmotionResponseDTO(emotion.emotion, emotion.value) })
    }

    private fun findUser(userId: Long): UserEntity {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        return user.get()
    }
}

