package co.bearus.magcloud.repository

import co.bearus.magcloud.entity.UserDiaryEntity
import co.bearus.magcloud.entity.UserDiaryResultEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserDiaryResultRepository : JpaRepository<UserDiaryResultEntity, UserDiaryEntity> {
    fun findByDiaryId(id: Long): UserDiaryResultEntity?
}
