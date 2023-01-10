package co.bearus.magcloud.repository

import co.bearus.magcloud.entity.diary.UserDiaryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface JPAUserDiaryRepository : JpaRepository<UserDiaryEntity, Long> {
    fun getByIdAndDate(id: Long, date: LocalDate): UserDiaryEntity?
}
