package co.bearus.magcloud.domain.service

import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.exception.NotFoundException
import co.bearus.magcloud.domain.entity.TagEntity
import co.bearus.magcloud.domain.repository.JPATagRepository
import org.springframework.stereotype.Service

@Service
class TagService(private val repository: JPATagRepository) {
    fun getAllTag(): List<co.bearus.magcloud.controller.dto.response.TagResponseDTO> {
        return repository.findAll().map { co.bearus.magcloud.controller.dto.response.TagResponseDTO(it.id!!, it.name) }
    }

    fun findTagById(id: Long): co.bearus.magcloud.controller.dto.response.TagResponseDTO {
        val tag = repository.findById(id)
        if (!tag.isPresent) throw NotFoundException("그런 태그는 찾을 수 없습니다")
        return co.bearus.magcloud.controller.dto.response.TagResponseDTO(tag.get().id!!, tag.get().name)
    }

    fun createNewTag(dto: co.bearus.magcloud.controller.dto.request.TagCreateDTO): co.bearus.magcloud.controller.dto.response.TagResponseDTO {
        val previous = repository.getTagEntityByName(dto.name)
        if (previous != null) throw DomainException("같은 이름의 태그가 이미 존재합니다")
        val newTag = TagEntity(name = dto.name)
        val result = repository.save(newTag)
        return co.bearus.magcloud.controller.dto.response.TagResponseDTO(result.id!!, result.name)
    }
}
