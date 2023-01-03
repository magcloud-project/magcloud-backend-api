package hackathon.redbeanbackend.entity

import hackathon.redbeanbackend.dto.response.DiaryResultDTO
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.PrimaryKeyJoinColumn

@Entity(name = "user_diary_result")
data class UserDiaryResultEntity(
    @Id val id: Long? = null,
    @OneToOne @PrimaryKeyJoinColumn(name = "diary_id") var diary: UserDiaryEntity? = null,
    var stress: Float = 0.0f,
    var anxious: Float = 0.0f,
    var normal: Float = 0.0f,
    var lonely: Float = 0.0f,
) {
    constructor() : this(null, null)
    constructor(diary: UserDiaryEntity, stress: Float, anxious: Float, normal: Float, lonley: Float) : this(
        null,
        diary,
        stress,
        anxious,
        normal,
        lonley
    )

    fun toDTO() = DiaryResultDTO(diary!!.id!!, stress, anxious, normal, lonely)
}
