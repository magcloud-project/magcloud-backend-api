package hackathon.redbeanbackend.entity

import jakarta.persistence.*

@Entity(name = "user_diary_result")
data class UserDiaryResultEntity(
    @Id @GeneratedValue var id: Long? = null,
    @OneToOne @JoinColumn(name = "diary_id") var diary: UserDiaryEntity? = null,
    var stress: Float = 0.0f,
    var anxious: Float = 0.0f,
    var normal: Float = 0.0f,
    var lonely: Float = 0.0f,
) {
    constructor() : this(null, null)
    constructor(diary: UserDiaryEntity) : this(null, diary)
    constructor(diary: UserDiaryEntity, stress: Float, anxious: Float, normal: Float, lonley: Float) : this(
        null,
        diary,
        stress,
        anxious,
        normal,
        lonley
    )

    fun toDTO() = hackathon.redbeanbackend.dto.DiaryResultDTO(diary!!.id!!, stress, anxious, normal, lonely)
}
