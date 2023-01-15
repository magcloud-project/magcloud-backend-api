package co.bearus.magcloud.service.user

import co.bearus.magcloud.domain.NotFoundException
import co.bearus.magcloud.dto.response.APIResponse
import co.bearus.magcloud.dto.response.TagResponseDTO
import co.bearus.magcloud.entity.UserTagEntity
import co.bearus.magcloud.entity.user.UserEntity
import co.bearus.magcloud.repository.JPATagRepository
import co.bearus.magcloud.repository.JPAUserRepository
import org.springframework.stereotype.Service

@Service
class UserTagService(private val userRepository: JPAUserRepository, private val tagRepository: JPATagRepository) {
    fun addTagToUser(userId: Long, tagId: Long): APIResponse {
        val user = findUser(userId)
        val tagData = tagRepository.findById(tagId)
        if (!tagData.isPresent) throw NotFoundException("그런 태그는 찾을 수 없습니다")
        user.tags.add(UserTagEntity(tagData.get(), user))
        userRepository.save(user)
        return APIResponse.ok("태그가 추가되었습니다")
    }

    fun deleteTagOfUser(userId: Long, tagId: Long): APIResponse {
        val user = findUser(userId)
        userRepository.save(user)
        return APIResponse.ok("태그가 삭제되었습니다")
    }

    fun getTagsOfUser(userId: Long): List<TagResponseDTO> {
        val user = findUser(userId)
        return user.tags.map { TagResponseDTO(it.tagId!!.id!!, it.tagId!!.name) }
    }

    private fun findUser(userId: Long): UserEntity {
        val user = userRepository.findById(userId)
        if (!user.isPresent) throw NotFoundException("그런 유저는 찾을 수 없습니다")
        return user.get()
    }
}
