package co.bearus.magcloud.entity.user

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.PrimaryKeyJoinColumn

@Entity(name = "user_token")
class UserTokenEntity(
    @Id val id: Long? = null,
    @OneToOne @PrimaryKeyJoinColumn(name = "user_id") var user: UserEntity? = null,
    var refreshToken: String
) {
    constructor() : this(null, null, "")
}
