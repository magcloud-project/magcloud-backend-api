package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.friend.FriendRequestEntity
import co.bearus.magcloud.domain.entity.friend.FriendRequestEntityKey
import org.springframework.data.jpa.repository.JpaRepository

interface JPAFriendRequestRepository : JpaRepository<FriendRequestEntity, FriendRequestEntityKey> {
    fun countByToUserId(toUserId: String): Long
}
