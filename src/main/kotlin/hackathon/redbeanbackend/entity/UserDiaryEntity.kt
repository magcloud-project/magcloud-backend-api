package hackathon.redbeanbackend.entity

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(name = "user_diary")
data class UserDiaryEntity(
    @Id @GeneratedValue var id: Long? = null,
    @Column(name = "date") var date: LocalDate,
    @Column(length = 50000) var content: String,
    @ManyToOne @JoinColumn(name = "user_id") var user: UserEntity? = null,
    @OneToOne(mappedBy = "diary") var result: UserDiaryResultEntity? = null,
) : Serializable, BaseAuditEntity() {
    constructor() : this(0, LocalDate.now(), "")
    constructor(content: String, user: UserEntity, date: LocalDate) : this(null, date, content, user)

}
