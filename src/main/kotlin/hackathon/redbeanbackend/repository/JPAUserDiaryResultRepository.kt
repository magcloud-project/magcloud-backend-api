package hackathon.redbeanbackend.repository

import hackathon.redbeanbackend.entity.UserDiaryResultEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserDiaryResultRepository : JpaRepository<UserDiaryResultEntity, Long> {
    fun findByDiaryId(id: Long): UserDiaryResultEntity?
}
