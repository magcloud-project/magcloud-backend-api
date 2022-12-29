package hackathon.redbeanbackend.service

import hackathon.redbeanbackend.domain.DomainException
import hackathon.redbeanbackend.domain.NotFoundException
import hackathon.redbeanbackend.dto.APIResponse
import hackathon.redbeanbackend.dto.DiaryResponseDTO
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
    private val inferenceService: InferenceService){
    fun addDiary(userId: Long, content: String): APIResponse {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")

        val previousDiaries = getDiariesByDate(userId, getToday())
        if (previousDiaries.isNotEmpty()) throw DomainException("오늘의 일기는 이미 작성했습니다")

        val userDiary = UserDiaryEntity(content, user.get())
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
    fun getDiariesByDate(userId: Long,date: String): List<DiaryResponseDTO> {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        return user.get().diaries.filter { compareDate(it.createdAt, date) }.map { DiaryResponseDTO(it.id!!, it.content, it.createdAt, it.result?.toDTO()) }
    }
    fun compareDate(date: LocalDateTime, originDate: String): Boolean{
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) == originDate
    }
}

