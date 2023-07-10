package co.bearus.magcloud.domain.projection

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate

data class YearlyEmotionProjection @QueryProjection constructor(
    val month: Int,
    val emotion: String,
    val count: Long,
)
