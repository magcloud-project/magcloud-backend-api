package co.bearus.magcloud.domain.entity.diary

import co.bearus.magcloud.advice.SHA256
import co.bearus.magcloud.domain.entity.BaseAuditEntity
import co.bearus.magcloud.domain.entity.user.UserEntity
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDate

@Entity(name = "user_diary")
data class UserDiaryEntity(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column(name = "date")
    var date: LocalDate,

    @Column(length = 50000)
    var content: String,

    @Column(name = "content_hash", length = 256)
    var contentHash: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity? = null,

    @OneToMany(
        mappedBy = "diary",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    ) var emotions: MutableSet<UserDiaryEmotionEntity> = mutableSetOf(),
) : Serializable, BaseAuditEntity()
