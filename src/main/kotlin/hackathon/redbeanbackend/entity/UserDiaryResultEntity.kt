package hackathon.redbeanbackend.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "user_diary_result")
data class UserDiaryResultEntity(
    @Id @GeneratedValue var id: Long? = null,
    @OneToOne @JoinColumn(name = "diary_id") var diary: UserDiaryEntity? = null,
    var sadness: Float = 0.0f,
    var joy: Float = 0.0f,
    var naturality: Float = 0.0f,
    var anger: Float = 0.0f,
    var fear: Float = 0.0f,
    var depression: Float = 0.0f,
) {
    constructor() : this(null, null)
    constructor(diary: UserDiaryEntity) : this(null, diary)
    constructor(diary: UserDiaryEntity, sadness: Float, joy: Float, natural: Float, anger: Float, fear: Float, depression: Float) : this(null, diary, sadness, joy, natural, anger, fear, depression)
    fun toDTO() = hackathon.redbeanbackend.dto.DiaryResultDTO(diary!!.id!!, sadness, joy, naturality, anger, fear, depression)
}
