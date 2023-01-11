package co.bearus.magcloud.entity.user

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity(name = "user_token")
class UserTokenEntity(
    @Id val id: Long? = null,
    @OneToOne @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id") var user: UserEntity? = null,
    var refreshToken: String
) {
    constructor() : this(null, null, "")
}
