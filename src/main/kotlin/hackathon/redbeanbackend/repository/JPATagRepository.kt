package hackathon.redbeanbackend.repository

import hackathon.redbeanbackend.entity.TagEntity
import hackathon.redbeanbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPATagRepository: JpaRepository<TagEntity, Long> {
    fun getTagEntityByName(name: String): TagEntity?
}
