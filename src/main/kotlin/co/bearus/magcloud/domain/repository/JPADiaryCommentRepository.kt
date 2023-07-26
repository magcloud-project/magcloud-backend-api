package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.diary.DiaryCommentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPADiaryCommentRepository : JpaRepository<DiaryCommentEntity, String> {
    fun findAllByDiaryId(diaryId: String): List<DiaryCommentEntity>
}
