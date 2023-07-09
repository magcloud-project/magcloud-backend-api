package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.user.QUserEntity.Companion.userEntity
import co.bearus.magcloud.domain.entity.friend.QFriendEntity.Companion.friendEntity
import co.bearus.magcloud.domain.projection.QFriendUserProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

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
}
