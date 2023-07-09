package co.bearus.magcloud.domain.entity.user


import co.bearus.magcloud.domain.entity.BaseAuditEntity
import co.bearus.magcloud.domain.type.LoginProvider
import jakarta.persistence.*
import java.io.Serializable

@IdClass(UserSocialEntityKey::class)
@Entity(name = "social_user")
class UserSocialEntity private constructor(
    @Id
    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    val provider: LoginProvider,

    @Id
    @Column(name = "identifier")
    val socialIdentifier: String,

    @Column(name = "user_id")
    val userId: String,
) : Serializable, BaseAuditEntity() {
    companion object {
        fun newInstance(
            provider: LoginProvider,
            socialIdentifier: String,
            userId: String,
        ): UserSocialEntity {
            return UserSocialEntity(
                provider = provider,
                socialIdentifier = socialIdentifier,
                userId = userId,
            )
        }
    }
}
