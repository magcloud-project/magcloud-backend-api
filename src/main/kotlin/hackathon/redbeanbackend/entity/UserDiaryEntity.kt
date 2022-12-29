package hackathon.redbeanbackend.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name="user_diary")
data class UserDiaryEntity(
    @Id @GeneratedValue var id: Long? = null,
    var content: String,
    var createdAt: LocalDateTime,
    @ManyToOne @JoinColumn(name = "user_id") var user: UserEntity? = null,
){
    constructor() : this(0, "", LocalDateTime.now())
    constructor(content: String, user: UserEntity) : this(null, content, LocalDateTime.now(), user)
}
