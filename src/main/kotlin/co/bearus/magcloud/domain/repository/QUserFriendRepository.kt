package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.diary.QDiaryEntity.Companion.diaryEntity
import co.bearus.magcloud.domain.entity.friend.QFriendEntity.Companion.friendEntity
import co.bearus.magcloud.domain.entity.friend.QFriendRequestEntity.Companion.friendRequestEntity
import co.bearus.magcloud.domain.entity.user.QUserEntity.Companion.userEntity
import co.bearus.magcloud.domain.entity.user.QUserNotificationConfigEntity.Companion.userNotificationConfigEntity
import co.bearus.magcloud.domain.projection.QDailyUserProjection
import co.bearus.magcloud.domain.projection.QFriendUserProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class QUserFriendRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun getFriends(
        userId: String,
    ) = queryFactory
        .selectFrom(friendEntity)
        .leftJoin(userEntity).on(friendEntity.toUserId.eq(userEntity.userId))
        .select(
            QFriendUserProjection(
                userId = userEntity.userId,
                name = userEntity.name,
                tag = userEntity.tag,
                profileImageUrl = userEntity.profileImageUrl,
                isDiaryShared = friendEntity.isDiaryAllowed,
            )
        )
        .where(friendEntity.fromUserId.eq(userId))
        .fetch()

    fun getDailyFriends(
        userId: String,
        date: LocalDate,
    ) = queryFactory
        .selectFrom(friendEntity)
        .select(
            QDailyUserProjection(
                userId = userEntity.userId,
                name = userEntity.name,
                tag = userEntity.tag,
                profileImageUrl = userEntity.profileImageUrl,
                emotion = diaryEntity.emotion,
                isDiaryAllowed = friendEntity.isDiaryAllowed,
                updatedAt = diaryEntity.updatedAt,
            )
        )
        .leftJoin(userEntity).on(friendEntity.fromUserId.eq(userEntity.userId))
        .leftJoin(diaryEntity).on(
            friendEntity.fromUserId.eq(diaryEntity.userId).and(
                diaryEntity.date.eq(date)
            )
        )
        .where(
            friendEntity.toUserId.eq(userId)
        )
        .fetch()

    fun getFriendRequests(
        userId: String,
    ) = queryFactory
        .selectFrom(friendRequestEntity)
        .leftJoin(userEntity).on(friendRequestEntity.fromUserId.eq(userEntity.userId))
        .select(
            userEntity
        )
        .where(friendRequestEntity.toUserId.eq(userId))
        .fetch()

    fun getSentFriendRequests(
        userId: String,
    ) = queryFactory
        .selectFrom(friendRequestEntity)
        .leftJoin(userEntity).on(friendRequestEntity.toUserId.eq(userEntity.userId))
        .select(
            userEntity
        )
        .where(friendRequestEntity.fromUserId.eq(userId))
        .fetch()

    fun getFeedNotificationEnabledFriends(
        userId: String,
    ) = queryFactory
        .selectFrom(friendEntity)
        .leftJoin(userEntity).on(friendEntity.toUserId.eq(userEntity.userId))
        .leftJoin(userNotificationConfigEntity).on(userNotificationConfigEntity.userId.eq(friendEntity.toUserId))
        .select(
            userEntity.userId
        )
        .where(friendEntity.fromUserId.eq(userId).and(userNotificationConfigEntity.feedEnabled.eq(true)))
        .fetch()

}
