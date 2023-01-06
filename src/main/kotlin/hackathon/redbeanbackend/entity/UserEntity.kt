package hackathon.redbeanbackend.entity

import hackathon.redbeanbackend.domain.LoginProvider
import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "user")
data class UserEntity(
    @Id @GeneratedValue var id: Long? = null,
    @Enumerated(value = EnumType.STRING) var provider: LoginProvider = LoginProvider.LOCAL,
    @Column(updatable = false) var userIdentifier: String,
    var email: String,
    var password: String,
    var name: String,
    @OneToOne(mappedBy = "user") var token: UserTokenEntity? = null,
    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    ) var tags: MutableList<UserTagEntity> = mutableListOf(),
    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    ) var devices: MutableList<UserDeviceEntity> = mutableListOf(),
    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    ) var diaries: MutableList<UserDiaryEntity> = mutableListOf()
): Serializable, BaseAuditEntity() {
    constructor() : this(null, LoginProvider.LOCAL,"", "", "", "", null, mutableListOf())
    constructor(provider: LoginProvider, identifier: String, email: String, password: String, name: String) : this(
        null,
        provider,
        identifier,
        email,
        password,
        name
    )

}
