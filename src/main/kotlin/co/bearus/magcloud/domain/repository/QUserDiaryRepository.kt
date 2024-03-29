package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.diary.QDiaryCommentEntity.Companion.diaryCommentEntity
import co.bearus.magcloud.domain.entity.diary.QDiaryEntity.Companion.diaryEntity
import co.bearus.magcloud.domain.entity.user.QUserEntity.Companion.userEntity
import co.bearus.magcloud.domain.projection.QDiaryCommentProjection
import co.bearus.magcloud.domain.projection.QDiaryIntegrityProjection
import co.bearus.magcloud.domain.projection.QMonthlyEmotionProjection
import co.bearus.magcloud.domain.projection.QYearlyEmotionProjection
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
                emotion = diaryEntity.emotion,
                createdAt = diaryEntity.createdAt,
                updatedAt = diaryEntity.updatedAt,
                date = diaryEntity.date,
            )
        )
        .fetchOne()

    fun getMonthlyDiaryIntegrity(
        userId: String,
        year: Int,
        month: Int,
    ) = queryFactory
        .selectFrom(diaryEntity)
        .where(
            diaryEntity.userId.eq(userId),
            diaryEntity.date.year().eq(year),
            diaryEntity.date.month().eq(month),
        )
        .select(
            QDiaryIntegrityProjection(
                diaryId = diaryEntity.diaryId,
                contentHash = diaryEntity.contentHash,
                emotion = diaryEntity.emotion,
                createdAt = diaryEntity.createdAt,
                updatedAt = diaryEntity.updatedAt,
                date = diaryEntity.date,
            )
        )
        .fetch()

    fun getUserMonthlyStatistics(
        userId: String,
        year: Int,
        month: Int,
    ) = queryFactory
        .selectFrom(diaryEntity)
        .where(
            diaryEntity.userId.eq(userId),
            diaryEntity.date.year().eq(year),
            diaryEntity.date.month().eq(month),
        )
        .select(
            QMonthlyEmotionProjection(
                date = diaryEntity.date,
                emotion = diaryEntity.emotion,
            )
        )
        .fetch()

    fun getUserYearlyStatistics(
        userId: String,
        year: Int,
    ) = queryFactory
        .selectFrom(diaryEntity)
        .where(
            diaryEntity.userId.eq(userId),
            diaryEntity.date.year().eq(year),
        )
        .select(
            QYearlyEmotionProjection(
                month = diaryEntity.date.month(),
                emotion = diaryEntity.emotion,
                count = diaryEntity.emotion.count()
            )
        )
        .groupBy(diaryEntity.date.month(), diaryEntity.emotion)
        .fetch()

    fun getDiaryComments(
        diaryId: String,
    ) = queryFactory
        .selectFrom(diaryCommentEntity)
        .leftJoin(userEntity).on(diaryCommentEntity.userId.eq(userEntity.userId))
        .where(diaryCommentEntity.diaryId.eq(diaryId))
        .select(
            QDiaryCommentProjection(
                commentId = diaryCommentEntity.commentId,
                diaryId = diaryCommentEntity.diaryId,
                userId = diaryCommentEntity.userId,
                content = diaryCommentEntity.content,
                createdAt = diaryCommentEntity.createdAt,
                updatedAt = diaryCommentEntity.updatedAt,
                username = userEntity.name,
                profileImageUrl = userEntity.profileImageUrl,
            )
        )
        .fetch()
}
