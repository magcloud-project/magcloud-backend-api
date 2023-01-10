package co.bearus.magcloud.entity.diary

import co.bearus.magcloud.entity.BaseAuditEntity
import co.bearus.magcloud.entity.user.UserDeviceEntity
import co.bearus.magcloud.entity.user.UserEntity
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDate

@Entity(name = "user_diary")
data class UserDiaryEntity(
    @Id @GeneratedValue var id: Long? = null,
    @Column(name = "date") var date: LocalDate,
    @Column(length = 50000) var content: String,
    @ManyToOne @JoinColumn(name = "user_id") var user: UserEntity? = null,
    @OneToMany(
        mappedBy = "diary",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    ) var emotions: MutableSet<UserDiaryEmotionEntity> = mutableSetOf(),
) : Serializable, BaseAuditEntity() {
    constructor() : this(0, LocalDate.now(), "")
    constructor(content: String, user: UserEntity, date: LocalDate) : this(null, date, content, user)

}
