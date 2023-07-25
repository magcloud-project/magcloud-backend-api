package co.bearus.magcloud.domain.entity.diary

import co.bearus.magcloud.controller.dto.request.DiaryCommentDTO
import co.bearus.magcloud.domain.entity.BaseAuditEntity
import co.bearus.magcloud.util.DateUtils.Companion.toEpochMillis
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable

@Entity(name = "diary_comment")
@DynamicInsert
@DynamicUpdate
class DiaryCommentEntity private constructor(
    @Id
    @Column(name = "diary_comment_id")
    val commentId: String,

    @Column(name = "diary_id")
    val diaryId: String,

    @Column(name = "user_id")
    val userId: String,

    @Column(name = "content")
    val content: String,
) : Serializable, BaseAuditEntity() {
    companion object {
        fun createNewComment(
            commentId: String,
            diaryId: String,
            userId: String,
            content: String,
        ) = DiaryCommentEntity(
            commentId = commentId,
            diaryId = diaryId,
            userId = userId,
            content = content,
        )
    }

    fun toDto() = DiaryCommentDTO(
        commentId = this.commentId,
        diaryId = this.diaryId,
        userId = this.userId,
        username = "",
        profileImageUrl = "",
        content = this.content,
        createdAtTs = this.createdAt!!.toEpochMillis(),
        updatedAtTs = this.updatedAt!!.toEpochMillis(),
    )
}
