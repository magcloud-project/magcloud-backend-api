package co.bearus.magcloud.domain.entity.user

import co.bearus.magcloud.controller.dto.response.UserDTO
import co.bearus.magcloud.domain.entity.BaseAuditEntity
import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "user")
class UserEntity private constructor(
    @Id
    @Column(name = "user_id")
    val userId: String,

    @Column(length = 255, name = "email")
    var email: String,

    @Column(length = 128, name = "name")
    var name: String,

    @Column(length = 4, name = "tag")
    var tag: String,

    @Column(length = 255, name = "profile_image_url")
    var profileImageUrl: String,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    var token: MutableList<UserTokenEntity> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "user_id")
    var devices: MutableSet<UserDeviceEntity> = mutableSetOf(),
) : Serializable, BaseAuditEntity() {
    fun toDto() = UserDTO(
        userId = this.userId,
        name = this.name,
        nameTag = "${this.name}#${this.tag}",
        profileImageUrl = this.profileImageUrl,
    )

    companion object {
        fun newInstance(
            userId: String,
            email: String,
            name: String,
            tag: String,
            profileImageUrl: String,
        ) = UserEntity(
            userId = userId,
            email = email,
            name = name,
            tag = tag,
            profileImageUrl = profileImageUrl,
        )

    }
}
