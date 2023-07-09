package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.diary.QDiaryEntity.Companion.diaryEntity
import co.bearus.magcloud.domain.projection.QDiaryIntegrityProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class QUserDiaryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun getDiaryIntegrityProjection(
        diaryId: String,
    ) = queryFactory
        .selectFrom(diaryEntity)
        .where(diaryEntity.diaryId.eq(diaryId))
        .select(
            QDiaryIntegrityProjection(
                diaryId = diaryEntity.diaryId,
                contentHash = diaryEntity.contentHash,
                createdAt = diaryEntity.createdAt,
                updatedAt = diaryEntity.updatedAt,
            )
        )
        .fetchOne()
}
