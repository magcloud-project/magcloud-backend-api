package co.bearus.magcloud.domain.entity.diary

import co.bearus.magcloud.advice.SHA256
import co.bearus.magcloud.controller.dto.request.DiaryPatchDTO
import co.bearus.magcloud.controller.dto.response.DiaryResponseDTO
import co.bearus.magcloud.domain.entity.BaseAuditEntity
import co.bearus.magcloud.util.DateUtils.Companion.toEpochMillis
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.io.Serializable
import java.time.LocalDate

@Entity(name = "diary")
class DiaryEntity private constructor(
    @Id
    @Column(name = "diary_id")
    val diaryId: String,

    @Column(name = "user_id")
    val userId: String,

    @Column(name = "ymd")
    val date: LocalDate,

    @Column(name = "emotion")
    var emotion: String,

    @Column(length = 50000)
    var content: String,

    @Column(name = "content_hash", length = 256)
    var contentHash: String,
) : Serializable, BaseAuditEntity() {
    fun updateDiary(dto: DiaryPatchDTO) {
        this.content = dto.content
        this.contentHash = SHA256.encrypt(dto.content)
        this.emotion = dto.emotion.name
    }

    fun toDto() = DiaryResponseDTO(
        diaryId = this.diaryId,
        userId = this.userId,
        date = this.date.toString(),
        emotion = this.emotion,
        content = this.content,
        contentHash = this.contentHash,
        createdAtTs = this.createdAt?.toEpochMillis() ?: 0,
        updateAtTs = this.updatedAt?.toEpochMillis() ?: 0,
    )

    companion object {
        fun createNewDiary(
            diaryId: String,
            userId: String,
            date: LocalDate,
            emotion: String,
            content: String,
            contentHash: String,
        ) = DiaryEntity(
            diaryId = diaryId,
            userId = userId,
            date = date,
            emotion = emotion,
            content = content,
            contentHash = contentHash,
        )
    }
}
