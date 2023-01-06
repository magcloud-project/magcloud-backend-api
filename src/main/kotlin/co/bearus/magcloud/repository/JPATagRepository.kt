package co.bearus.magcloud.repository

import co.bearus.magcloud.entity.TagEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPATagRepository : JpaRepository<TagEntity, Long> {
    fun getTagEntityByName(name: String): TagEntity?
}
