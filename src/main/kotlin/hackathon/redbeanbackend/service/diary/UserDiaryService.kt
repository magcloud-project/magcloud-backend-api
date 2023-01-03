package hackathon.redbeanbackend.service.diary

import hackathon.redbeanbackend.domain.NotFoundException
import hackathon.redbeanbackend.dto.response.APIResponse
import hackathon.redbeanbackend.dto.response.DiaryResponseDTO
import hackathon.redbeanbackend.entity.UserDiaryEntity
import hackathon.redbeanbackend.repository.JPAUserDiaryRepository
import hackathon.redbeanbackend.repository.JPAUserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
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

        val previousDiaries =
            diaryRepository.getByIdAndDate(userId, LocalDate.parse(diaryDate, DateTimeFormatter.BASIC_ISO_DATE))
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
            LocalDate.parse(diaryDate, DateTimeFormatter.BASIC_ISO_DATE).atStartOfDay()
        )
        val requestedResult = diaryRepository.save(userDiary)
        inferenceService.requestInference(requestedResult)
        return APIResponse.ok("일기가 추가되었습니다")
    }

    private fun getToday(): String {
        val date = LocalDate.now()
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    }

    fun getDiariesOfUser(userId: Long): List<DiaryResponseDTO> {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        return user.get().diaries.map { DiaryResponseDTO(it.id!!, it.content, it.createdAt, it.result?.toDTO()) }
    }

    fun getDiariesByDate(userId: Long, date: String): List<DiaryResponseDTO> {
        val diary = diaryRepository.getByIdAndDate(userId, LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE))
            ?: return emptyList()
        return listOf(diary).map { DiaryResponseDTO(it.id!!, it.content, it.createdAt, it.result?.toDTO()) }
    }

    fun compareDate(date: LocalDateTime, originDate: String): Boolean {
        println("${date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))} $originDate")
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) == originDate
    }
}

