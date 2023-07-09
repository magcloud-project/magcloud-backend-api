package co.bearus.magcloud.domain.projection

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class DiaryIntegrityProjection @QueryProjection constructor(
    val diaryId: String,
    val contentHash: String,
    val emotion: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
