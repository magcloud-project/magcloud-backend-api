package co.bearus.magcloud.entity

import jakarta.persistence.*

@Entity(name = "user_token")
class UserTokenEntity(
    @Id val id: Long? = null,
    @OneToOne @PrimaryKeyJoinColumn(name = "user_id") var user: UserEntity? = null,
    var refreshToken: String
) {
    constructor() : this(null, null, "")
}
