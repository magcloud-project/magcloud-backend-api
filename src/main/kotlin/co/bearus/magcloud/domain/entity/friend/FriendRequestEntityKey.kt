package co.bearus.magcloud.domain.entity.friend

import java.io.Serializable

data class FriendRequestEntityKey(
    val fromUserId: String = "",
    val toUserId: String = "",
) : Serializable
