package hackathon.redbeanbackend.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "user_diary")
data class UserDiaryEntity(
    @Id @GeneratedValue var id: Long? = null,
    @Column(length = 50000) var content: String,
    var createdAt: LocalDateTime,
    @ManyToOne @JoinColumn(name = "user_id") var user: UserEntity? = null,
    @OneToOne(mappedBy = "diary") var result: UserDiaryResultEntity? = null,
) {
    constructor() : this(0, "", LocalDateTime.now())
    constructor(content: String, user: UserEntity, createdAt: LocalDateTime) : this(null, content, createdAt, user)

}
