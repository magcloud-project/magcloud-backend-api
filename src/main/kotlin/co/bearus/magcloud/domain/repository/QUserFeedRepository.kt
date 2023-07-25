package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.diary.QDiaryCommentEntity.Companion.diaryCommentEntity
import co.bearus.magcloud.domain.entity.diary.QDiaryEntity.Companion.diaryEntity
import co.bearus.magcloud.domain.entity.diary.QDiaryLikeEntity.Companion.diaryLikeEntity
import co.bearus.magcloud.domain.entity.friend.QFriendEntity.Companion.friendEntity
import co.bearus.magcloud.domain.entity.user.QUserEntity.Companion.userEntity
import co.bearus.magcloud.domain.projection.FeedProjection
import co.bearus.magcloud.domain.projection.QFeedProjection
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class QUserFeedRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun getFeedByDiaryId(diaryId: String, userId: String,): FeedProjection? {
        return queryFactory
            .selectFrom(diaryEntity)
            .leftJoin(userEntity).on(diaryEntity.userId.eq(userEntity.userId))
            .leftJoin(diaryLikeEntity).on(diaryEntity.diaryId.eq(diaryLikeEntity.diaryId).and(diaryLikeEntity.userId.eq(userId)))
            .leftJoin(diaryCommentEntity).on(diaryEntity.diaryId.eq(diaryCommentEntity.diaryId))
            .where(diaryEntity.diaryId.eq(diaryId))
            .select(
                QFeedProjection(
                    userId = userEntity.userId,
                    userName = userEntity.name,
                    profileImageUrl = userEntity.profileImageUrl,
                    diaryId = diaryEntity.diaryId,
                    mood = diaryEntity.emotion,
                    ymd = diaryEntity.date,
                    content = diaryEntity.content,
                    likeCount = diaryEntity.likeCount,
                    commentCount = diaryCommentEntity.count().intValue(),
                    imageUrl = diaryEntity.imageUrl,
                    likedAt = diaryLikeEntity.createdAt,
                    createdAt = diaryEntity.createdAt,
                )
            )
            .groupBy(diaryEntity.diaryId)
            .fetchOne()
    }

    fun getUserFeed(
        userId: String,
        baseId: String?,
        size: Int,
    ): List<FeedProjection> {
        val friendIds = queryFactory
            .selectFrom(friendEntity)
            .select(friendEntity.fromUserId)
            .where(friendEntity.toUserId.eq(userId).and(friendEntity.isDiaryAllowed.eq(true)))
            .fetch()
            .toMutableList()
        friendIds.add(userId)

        val predicate =
            if (baseId == null)
                diaryEntity.userId.`in`(friendIds)
            else
                diaryEntity.userId.`in`(friendIds).and(diaryEntity.diaryId.lt(baseId))

        return queryFactory
            .selectFrom(diaryEntity)
            .leftJoin(userEntity).on(diaryEntity.userId.eq(userEntity.userId))
            .leftJoin(diaryLikeEntity).on(diaryEntity.diaryId.eq(diaryLikeEntity.diaryId).and(diaryLikeEntity.userId.eq(userId)))
            .leftJoin(diaryCommentEntity).on(diaryEntity.diaryId.eq(diaryCommentEntity.diaryId))
            .where(predicate)
            .select(
                QFeedProjection(
                    userId = userEntity.userId,
                    userName = userEntity.name,
                    profileImageUrl = userEntity.profileImageUrl,
                    diaryId = diaryEntity.diaryId,
                    mood = diaryEntity.emotion,
                    ymd = diaryEntity.date,
                    content = diaryEntity.content,
                    imageUrl = diaryEntity.imageUrl,
                    likeCount = diaryEntity.likeCount,
                    commentCount = diaryCommentEntity.count().intValue(),
                    likedAt = diaryLikeEntity.createdAt,
                    createdAt = diaryEntity.createdAt,
                )
            )
            .groupBy(diaryEntity.diaryId)
            .orderBy(diaryEntity.diaryId.desc())
            .limit(size.toLong())
            .fetch()
    }

    fun getFriendFeed(
        baseId: String?,
        size: Int,
        myId: String,
        friendId: String,
    ): List<FeedProjection> = queryFactory
        .selectFrom(diaryEntity)
        .leftJoin(userEntity).on(diaryEntity.userId.eq(userEntity.userId))
        .leftJoin(diaryLikeEntity).on(diaryEntity.diaryId.eq(diaryLikeEntity.diaryId).and(diaryLikeEntity.userId.eq(myId)))
        .leftJoin(diaryCommentEntity).on(diaryEntity.diaryId.eq(diaryCommentEntity.diaryId))
        .where(*friendFeedCondition(baseId, friendId))
        .select(
            QFeedProjection(
                userId = userEntity.userId,
                userName = userEntity.name,
                profileImageUrl = userEntity.profileImageUrl,
                diaryId = diaryEntity.diaryId,
                mood = diaryEntity.emotion,
                ymd = diaryEntity.date,
                content = diaryEntity.content,
                likeCount = diaryEntity.likeCount,
                commentCount = diaryCommentEntity.count().intValue(),
                imageUrl = diaryEntity.imageUrl,
                likedAt = diaryLikeEntity.createdAt,
                createdAt = diaryEntity.createdAt,
            )
        )
        .groupBy(diaryEntity.diaryId)
        .orderBy(diaryEntity.diaryId.desc())
        .limit(size.toLong())
        .fetch()

    private fun friendFeedCondition(baseId: String?, friendId: String): Array<BooleanExpression> {
        val list = mutableListOf<BooleanExpression>()
        list.add(diaryEntity.userId.eq(friendId))
        if (baseId != null) list.add(diaryEntity.diaryId.lt(baseId))
        return list.toTypedArray()
    }

}
