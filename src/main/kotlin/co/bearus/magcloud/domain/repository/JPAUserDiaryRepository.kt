package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.diary.UserDiaryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface JPAUserDiaryRepository : JpaRepository<UserDiaryEntity, Long> {
    fun getByUserIdAndDate(userId: Long, date: LocalDate): UserDiaryEntity?
}
