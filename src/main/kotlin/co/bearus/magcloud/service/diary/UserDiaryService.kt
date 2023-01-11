package co.bearus.magcloud.service.diary

import co.bearus.magcloud.domain.NotFoundException
import co.bearus.magcloud.dto.response.APIResponse
import co.bearus.magcloud.dto.response.DiaryResponseDTO
import co.bearus.magcloud.dto.response.EmotionResponseDTO
import co.bearus.magcloud.entity.diary.UserDiaryEntity
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
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")

        val date = LocalDate.parse(diaryDate, DateTimeFormatter.BASIC_ISO_DATE)
        val previousDiaries =
            diaryRepository.getByUserIdAndDate(userId, date)
        if (previousDiaries != null) {
            //UPDATE CODE
            previousDiaries.content = content
            diaryRepository.save(previousDiaries)
            inferenceService.requestInference(previousDiaries)
            return APIResponse.ok("일기가 수정되었습니다.");
        }

        val userDiary = UserDiaryEntity(
            content,
            user.get(),
            date
        )
        val requestedResult = diaryRepository.save(userDiary)
        inferenceService.requestInference(requestedResult)
        return APIResponse.ok("일기가 추가되었습니다")
    }

    fun getDiariesOfUser(userId: Long): List<DiaryResponseDTO> {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        return user.get().diaries.map {
            DiaryResponseDTO(
                it.id!!,
                it.content,
                it.date.atStartOfDay(),
                it.emotions.map { emotion -> EmotionResponseDTO(emotion.emotion, emotion.value) }
            )
        }
    }

    fun getDiaryByDate(userId: Long, date: String): DiaryResponseDTO? {
        val diary = diaryRepository.getByUserIdAndDate(userId, LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE))
            ?: throw NotFoundException()
        return DiaryResponseDTO(
            diary.id!!,
            diary.content,
            diary.date.atStartOfDay(),
            diary.emotions.map { emotion -> EmotionResponseDTO(emotion.emotion, emotion.value) })
    }

}

