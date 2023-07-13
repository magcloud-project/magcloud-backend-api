package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.diary.DiaryLikeEntity
import co.bearus.magcloud.domain.entity.diary.DiaryLikeEntityKey
import org.springframework.data.jpa.repository.JpaRepository

interface JPADiaryLikeRepository : JpaRepository<DiaryLikeEntity, DiaryLikeEntityKey> {
    fun countByDiaryId(diaryId: String): Long
}
