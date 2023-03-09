package co.bearus.magcloud.domain.entity.user

import co.bearus.magcloud.domain.type.LoginProvider
import co.bearus.magcloud.domain.entity.BaseAuditEntity
import co.bearus.magcloud.domain.entity.UserTagEntity
import co.bearus.magcloud.domain.entity.diary.UserDiaryEntity
import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "user")
@Table(indexes = [Index(name = "idx_user_identity_provider", columnList = "provider, userIdentifier", unique = true)])
data class UserEntity(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Enumerated(value = EnumType.STRING)
    var provider: LoginProvider = LoginProvider.LOCAL,

    @Column(updatable = false)
    var userIdentifier: String,

    @Column(length = 255, name = "email")
    var email: String,

    @Column(length = 255, name = "password")
    var password: String,

    @Column(length = 128, name = "name")
    var name: String,

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "user")
    var token: UserTokenEntity? = null,

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var tags: MutableList<UserTagEntity> = mutableListOf(),

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var devices: MutableSet<UserDeviceEntity> = mutableSetOf(),

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var diaries: MutableList<UserDiaryEntity> = mutableListOf()
) : Serializable, BaseAuditEntity() {
    companion object {
        fun createNewUser(loginProvider: LoginProvider, email: String, identifier: String, password: String, name: String) = UserEntity (
            provider = loginProvider,
            email = email,
            userIdentifier = identifier,
            password = password,
            name = name,
                )

    }
}
