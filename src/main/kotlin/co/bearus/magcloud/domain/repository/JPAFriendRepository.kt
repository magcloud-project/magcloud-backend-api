package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.friend.FriendEntity
import co.bearus.magcloud.domain.entity.friend.FriendEntityKey
import org.springframework.data.jpa.repository.JpaRepository

interface JPAFriendRepository : JpaRepository<FriendEntity, FriendEntityKey> {
    fun deleteAllByFromUserId(fromUserId: String)
    fun deleteAllByToUserId(toUserId: String)
}
