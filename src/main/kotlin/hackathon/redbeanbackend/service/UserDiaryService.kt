package hackathon.redbeanbackend.service

import hackathon.redbeanbackend.domain.NotFoundException
import hackathon.redbeanbackend.dto.APIResponse
import hackathon.redbeanbackend.dto.DiaryResponseDTO
import hackathon.redbeanbackend.entity.UserDiaryEntity
import hackathon.redbeanbackend.repository.JPAUserRepository
import org.springframework.stereotype.Service

@Service
class UserDiaryService(private val userRepository: JPAUserRepository) {
    fun addDiary(userId: Long, content: String): APIResponse {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        user.get().diaries.add(UserDiaryEntity(content, user.get()))
        userRepository.save(user.get())
        return APIResponse.ok("일기가 추가되었습니다")
    }

    fun getDiariesOfUser(userId: Long): List<DiaryResponseDTO> {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        return user.get().diaries.map { DiaryResponseDTO(it.id!!, it.content, it.createdAt) }
    }
}
