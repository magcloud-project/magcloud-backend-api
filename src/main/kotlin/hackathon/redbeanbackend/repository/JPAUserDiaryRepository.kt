package hackathon.redbeanbackend.repository

import hackathon.redbeanbackend.entity.UserDiaryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface JPAUserDiaryRepository : JpaRepository<UserDiaryEntity, Long> {
    fun getByIdAndDate(id: Long, date: LocalDate): UserDiaryEntity?
}
