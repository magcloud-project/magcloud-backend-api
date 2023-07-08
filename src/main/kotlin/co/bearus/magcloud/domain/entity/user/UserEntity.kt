package co.bearus.magcloud.domain.entity.user

import co.bearus.magcloud.domain.entity.BaseAuditEntity
import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "user")
class UserEntity private constructor(
    @Id
    @Column(name = "id")
    val userId: String,

    @Column(length = 255, name = "email")
    var email: String,

    @Column(length = 128, name = "name")
    var name: String,

    @Column(length = 4, name = "tag")
    var tag: String,

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "user")
    var token: UserTokenEntity? = null,

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var devices: MutableSet<UserDeviceEntity> = mutableSetOf(),
) : Serializable, BaseAuditEntity() {
    companion object {
        fun newInstance(
            userId: String,
            email: String,
            name: String,
            tag: String,
        ) = UserEntity(
            userId = userId,
            email = email,
            name = name,
            tag = tag,
        )

    }
}
