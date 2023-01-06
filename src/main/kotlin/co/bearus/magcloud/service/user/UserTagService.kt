package co.bearus.magcloud.service.user

import co.bearus.magcloud.domain.NotFoundException
import co.bearus.magcloud.dto.response.APIResponse
import co.bearus.magcloud.dto.response.TagResponseDTO
import co.bearus.magcloud.entity.UserTagEntity
import co.bearus.magcloud.repository.JPATagRepository
import co.bearus.magcloud.repository.JPAUserRepository
import org.springframework.stereotype.Service

@Service
class UserTagService(private val userRepository: JPAUserRepository, private val tagRepository: JPATagRepository) {
    fun addTagToUser(userId: Long, tagId: Long): APIResponse {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
//        val tag = user.get().tags.find { it.id == tagId }
//        if (tag != null) throw DomainException("이미 추가된 태그입니다")

        val tagData = tagRepository.findById(tagId)
        if (!tagData.isPresent) throw NotFoundException("그런 태그는 찾을 수 없습니다")
        user.get().tags.add(UserTagEntity(tagData.get(), user.get()))
        userRepository.save(user.get())
        return APIResponse.ok("태그가 추가되었습니다")
    }

    fun deleteTagOfUser(userId: Long, tagId: Long): APIResponse {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        //   val tag = user.get().tags.find { it.id == tagId } ?: throw DomainException("그 태그는 해당 유저에게 없습니다.")

        // user.get().tags.remove(tag)
        userRepository.save(user.get())
        return APIResponse.ok("태그가 삭제되었습니다")
    }

    fun getTagsOfUser(userId: Long): List<TagResponseDTO> {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        return user.get().tags.map { TagResponseDTO(it.tagId!!.id!!, it.tagId!!.name) }
    }
}
