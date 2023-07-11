package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.diary.QDiaryEntity.Companion.diaryEntity
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
    fun getUserFeed(
        userId: String,
        baseId: String?,
        size: Int,
    ): List<FeedProjection> = queryFactory
        .selectFrom(friendEntity)
        .leftJoin(userEntity).on(friendEntity.fromUserId.eq(userEntity.userId))
        .leftJoin(diaryEntity).on(friendEntity.fromUserId.eq(diaryEntity.userId))
        .where(feedCondition(userId, baseId))
        .select(
            QFeedProjection(
                userId = userEntity.userId,
                userName = userEntity.name,
                profileImageUrl = userEntity.profileImageUrl,
                diaryId = diaryEntity.diaryId,
                mood = diaryEntity.emotion,
                ymd = diaryEntity.date,
                content = diaryEntity.content,
                createdAt = diaryEntity.createdAt,
            )
        )
        .orderBy(diaryEntity.diaryId.desc())
        .limit(size.toLong())
        .fetch()

    fun getFriendFeed(
        baseId: String?,
        size: Int,
        friendId: String,
    ): List<FeedProjection> = queryFactory
        .selectFrom(diaryEntity)
        .leftJoin(userEntity).on(diaryEntity.userId.eq(userEntity.userId))
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
                createdAt = diaryEntity.createdAt,
            )
        )
        .orderBy(diaryEntity.diaryId.desc())
        .limit(size.toLong())
        .fetch()

    private fun friendFeedCondition(baseId: String?, friendId: String): Array<BooleanExpression> {
        val list = mutableListOf<BooleanExpression>()
        list.add(diaryEntity.userId.eq(friendId))
        if (baseId != null) list.add(diaryEntity.diaryId.lt(baseId))
        return list.toTypedArray()
    }

    private fun feedCondition(userId: String, baseId: String?): BooleanExpression {
        val exp = if (baseId == null)
            friendEntity.toUserId.eq(userId).and(friendEntity.isDiaryAllowed.eq(true))
        else
            friendEntity.toUserId.eq(userId)
                .and(friendEntity.isDiaryAllowed.eq(true).and(diaryEntity.diaryId.lt(baseId)))
        return diaryEntity.userId.eq(userId).or(exp)
    }

}
