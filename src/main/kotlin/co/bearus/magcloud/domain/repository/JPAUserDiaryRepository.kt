package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.diary.DiaryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface JPAUserDiaryRepository : JpaRepository<DiaryEntity, String> {
    fun getByUserIdAndDate(userId: String, date: LocalDate): DiaryEntity?
}
