package co.bearus.magcloud.service

import co.bearus.magcloud.domain.DomainException
import co.bearus.magcloud.domain.NotFoundException
import co.bearus.magcloud.dto.request.TagCreateDTO
import co.bearus.magcloud.dto.response.TagResponseDTO
import co.bearus.magcloud.entity.TagEntity
import co.bearus.magcloud.repository.JPATagRepository
import org.springframework.stereotype.Service

@Service
class TagService(private val repository: JPATagRepository) {
    fun getAllTag(): List<TagResponseDTO> {
        return repository.findAll().map { TagResponseDTO(it.id!!, it.name) }
    }

    fun findTagById(id: Long): TagResponseDTO {
        val tag = repository.findById(id)
        if (!tag.isPresent) throw NotFoundException("그런 태그는 찾을 수 없습니다")
        return TagResponseDTO(tag.get().id!!, tag.get().name)
    }

    fun createNewTag(dto: TagCreateDTO): TagResponseDTO {
        val previous = repository.getTagEntityByName(dto.name!!)
        if (previous != null) throw DomainException("같은 이름의 태그가 이미 존재합니다")
        val newTag = TagEntity(dto.name)
        val result = repository.save(newTag)
        return TagResponseDTO(result.id!!, result.name)
    }
}
