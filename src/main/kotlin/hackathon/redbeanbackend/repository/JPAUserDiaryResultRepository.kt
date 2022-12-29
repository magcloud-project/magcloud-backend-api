package hackathon.redbeanbackend.repository

import hackathon.redbeanbackend.entity.UserDiaryEntity
import hackathon.redbeanbackend.entity.UserDiaryResultEntity
import hackathon.redbeanbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserDiaryResultRepository : JpaRepository<UserDiaryResultEntity, Long> {
    fun findByDiaryId(id: Long): UserDiaryResultEntity?
}
