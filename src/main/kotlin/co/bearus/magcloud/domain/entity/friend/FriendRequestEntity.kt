package co.bearus.magcloud.domain.entity.friend

import co.bearus.magcloud.domain.entity.BaseAuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable

@IdClass(FriendRequestEntityKey::class)
@Entity(name = "friend_request")
class FriendRequestEntity private constructor(
    @Id
    @Column(name = "from_user_id")
    val fromUserId: String,

    @Id
    @Column(name = "to_user_id")
    val toUserId: String,
) : Serializable, BaseAuditEntity() {
    companion object {
        fun newInstance(
            fromUserId: String,
            toUserId: String,
        ) = FriendRequestEntity(
            fromUserId = fromUserId,
            toUserId = toUserId,
        )
    }
}
