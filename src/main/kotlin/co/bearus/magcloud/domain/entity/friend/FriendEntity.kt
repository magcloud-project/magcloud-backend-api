package co.bearus.magcloud.domain.entity.friend

import co.bearus.magcloud.domain.entity.BaseAuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable

@IdClass(FriendEntityKey::class)
@Entity(name = "friend")
class FriendEntity private constructor(
    @Id
    @Column(name = "from_user_id")
    val fromUserId: String,

    @Id
    @Column(name = "to_user_id")
    val toUserId: String,

    @Column(name = "is_diary_allowed")
    var isDiaryAllowed: Boolean,
) : Serializable, BaseAuditEntity() {
    companion object {
        fun newInstance(
            fromUserId: String,
            toUserId: String,
            isDiaryAllowed: Boolean,
        ) = FriendEntity(
            fromUserId = fromUserId,
            toUserId = toUserId,
            isDiaryAllowed = isDiaryAllowed,
        )
    }
}
