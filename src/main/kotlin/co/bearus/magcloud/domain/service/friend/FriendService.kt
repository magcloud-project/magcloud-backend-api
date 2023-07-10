package co.bearus.magcloud.domain.service.friend

import co.bearus.magcloud.controller.dto.response.DailyUserDTO
import co.bearus.magcloud.controller.dto.response.FriendDTO
import co.bearus.magcloud.controller.dto.response.UserDTO
import co.bearus.magcloud.domain.entity.friend.FriendEntity
import co.bearus.magcloud.domain.entity.friend.FriendEntityKey
import co.bearus.magcloud.domain.entity.friend.FriendRequestEntity
import co.bearus.magcloud.domain.entity.friend.FriendRequestEntityKey
import co.bearus.magcloud.domain.exception.AlreadyFriendException
import co.bearus.magcloud.domain.exception.AlreadyFriendRequestedException
import co.bearus.magcloud.domain.exception.FriendRequestNotFoundException
import co.bearus.magcloud.domain.exception.NotFriendException
import co.bearus.magcloud.domain.repository.JPAFriendRepository
import co.bearus.magcloud.domain.repository.JPAFriendRequestRepository
import co.bearus.magcloud.domain.repository.QUserFriendRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class FriendService(
    private val friendRepository: JPAFriendRepository,
    private val friendRequestRepository: JPAFriendRequestRepository,
    private val qUserFriendRepository: QUserFriendRepository,
) {
    @Transactional
    fun requestFriend(
        fromUserId: String,
        toUserId: String,
    ) {
        if (isFriend(fromUserId, toUserId)) throw AlreadyFriendException()
        if (isFriendRequested(fromUserId, toUserId)) throw AlreadyFriendRequestedException()
        val newFriendRequest = FriendRequestEntity.newInstance(
            fromUserId = fromUserId,
            toUserId = toUserId,
        )
        friendRequestRepository.save(newFriendRequest)
    }

    @Transactional
    fun updateFriendShareableStatus(userId: String, friendId: String, shareable: Boolean) {
        val friend = friendRepository.findById(FriendEntityKey(userId, friendId))
            .orElseThrow { NotFriendException() }
        friend.isDiaryAllowed = shareable
        friendRepository.save(friend)
    }

    private fun removeRequest(
        fromUserId: String,
        toUserId: String,
    ) {
        friendRequestRepository.deleteById(FriendRequestEntityKey(fromUserId, toUserId))
    }

    @Transactional
    fun acceptFriendRequest(
        fromUserId: String, //친구 요청 기준 toUser
        toUserId: String, //친구 요청 기준 FromUser
    ) {
        if (isFriend(fromUserId, toUserId)) throw AlreadyFriendException()
        if (!isFriendRequested(toUserId, fromUserId)) throw FriendRequestNotFoundException()

        removeRequest(toUserId, fromUserId)
        val newFriendFirst = FriendEntity.newInstance(
            fromUserId = fromUserId,
            toUserId = toUserId,
            isDiaryAllowed = true,
        )
        val newFriendSecond = FriendEntity.newInstance(
            fromUserId = toUserId,
            toUserId = fromUserId,
            isDiaryAllowed = true,
        )
        friendRepository.save(newFriendFirst)
        friendRepository.save(newFriendSecond)
    }

    @Transactional
    fun rejectFriendRequest(
        fromUserId: String,
        toUserId: String,
    ) {
        if (!isFriendRequested(toUserId, fromUserId)) throw FriendRequestNotFoundException()
        removeRequest(toUserId, fromUserId)
    }

    @Transactional
    fun breakFriend(
        fromUserId: String,
        toUserId: String,
    ) {
        friendRepository.deleteById(FriendEntityKey(fromUserId, toUserId))
        friendRepository.deleteById(FriendEntityKey(toUserId, fromUserId))
    }

    fun getFriends(userId: String): List<FriendDTO> {
        return qUserFriendRepository
            .getFriends(userId)
            .map { it.toDto() }
    }

    fun getDailyFriends(userId: String): List<DailyUserDTO> {
        return qUserFriendRepository
            .getDailyFriends(userId, LocalDate.now())
            .map { it.toDto() }
    }

    fun getSentFriendRequests(userId: String): List<UserDTO> {
        return qUserFriendRepository
            .getSentFriendRequests(userId)
            .map { it.toDto() }
    }

    fun getFriendRequests(userId: String): List<UserDTO> {
        return qUserFriendRepository
            .getFriendRequests(userId)
            .map { it.toDto() }
    }

    fun countFriendRequests(userId: String) = friendRequestRepository.countByToUserId(userId)

    fun isFriend(fromUserId: String, toUserId: String) =
        friendRepository.existsById(FriendEntityKey(fromUserId, toUserId))

    fun isDiaryReadable(viewerId: String, victimId: String) = friendRepository
        .findById(FriendEntityKey(victimId, viewerId))
        .map {
            it.isDiaryAllowed
        }
        .orElse(false)

    fun isFriendRequested(fromUserId: String, toUserId: String) =
        friendRequestRepository.existsById(FriendRequestEntityKey(fromUserId, toUserId))
}
