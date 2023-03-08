package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.TagEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPATagRepository : JpaRepository<TagEntity, Long> {
    fun getTagEntityByName(name: String): TagEntity?
}
