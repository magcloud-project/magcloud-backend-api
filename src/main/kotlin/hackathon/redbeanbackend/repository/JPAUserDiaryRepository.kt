package hackathon.redbeanbackend.repository

import hackathon.redbeanbackend.entity.UserDiaryEntity
import hackathon.redbeanbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface JPAUserDiaryRepository : JpaRepository<UserDiaryEntity, Long>{
    @Query(value = "SELECT e.* FROM user_diary e WHERE e.user_id = :id AND DATE(e.created_at) =:date LIMIT 1", nativeQuery = true)
    fun getByIdAndDate(id: Long, date: LocalDate): UserDiaryEntity?
}
