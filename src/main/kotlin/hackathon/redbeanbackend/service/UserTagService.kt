package hackathon.redbeanbackend.service

import hackathon.redbeanbackend.domain.DomainException
import hackathon.redbeanbackend.domain.NotFoundException
import hackathon.redbeanbackend.dto.APIResponse
import hackathon.redbeanbackend.dto.TagResponseDTO
import hackathon.redbeanbackend.entity.UserTagEntity
import hackathon.redbeanbackend.repository.JPATagRepository
import hackathon.redbeanbackend.repository.JPAUserRepository
import org.springframework.stereotype.Service

@Service
class UserTagService(private val userRepository: JPAUserRepository, private val tagRepository: JPATagRepository) {
    fun addTagToUser(userId: Long, tagId: Long): APIResponse {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        val tag = user.get().tags.find { it.id == tagId }
        if (tag != null) throw DomainException("이미 추가된 태그입니다")

        val tagData = tagRepository.findById(tagId)
        if (!tagData.isPresent) throw NotFoundException("그런 태그는 찾을 수 없습니다")
        user.get().tags.add(UserTagEntity(tagData.get(), user.get()))
        userRepository.save(user.get())
        return APIResponse.ok("태그가 추가되었습니다")
    }

    fun deleteTagOfUser(userId: Long, tagId: Long): APIResponse {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        val tag = user.get().tags.find { it.id == tagId } ?: throw DomainException("그 태그는 해당 유저에게 없습니다.")

        user.get().tags.remove(tag)
        userRepository.save(user.get())
        return APIResponse.ok("태그가 삭제되었습니다")
    }

    fun getTagsOfUser(userId: Long): List<TagResponseDTO> {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        return user.get().tags.map { TagResponseDTO(it.tagId!!.id!!, it.tagId!!.name) }
    }
}
