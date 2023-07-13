package co.bearus.magcloud.domain.entity.diary

import co.bearus.magcloud.domain.entity.BaseAuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.io.Serializable
import java.time.LocalDate

@Entity(name = "diary_like")
class DiaryLikeEntity private constructor(
    @Id
    @Column(name = "diary_id")
    val diaryId: String,

    @Column(name = "user_id")
    val userId: String,
) : Serializable, BaseAuditEntity() {
    companion object {
        fun of(diaryId: String, userId: String) = DiaryLikeEntity(diaryId, userId)
    }
}
