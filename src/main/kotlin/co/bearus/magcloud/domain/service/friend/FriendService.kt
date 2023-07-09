package co.bearus.magcloud.domain.service.friend

import co.bearus.magcloud.domain.entity.friend.FriendEntityKey
import co.bearus.magcloud.domain.repository.JPAFriendRepository
import co.bearus.magcloud.domain.repository.JPAFriendRequestRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class FriendService(
    private val friendRepository: JPAFriendRepository,
    private val friendRequestRepository: JPAFriendRequestRepository,
) {
    @Transactional
    fun requestFriend(
        fromUserId: String,
        toUserId: String,
    ) {
        if(isFriend(fromUserId, toUserId)) {
            throw Exception("Already friends")
        }
    }

    fun isFriend(fromUserId: String, toUserId: String) =
        friendRepository.existsById(FriendEntityKey(fromUserId, toUserId))
}
