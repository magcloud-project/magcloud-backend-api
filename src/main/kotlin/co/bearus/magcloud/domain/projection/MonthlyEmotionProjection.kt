package co.bearus.magcloud.domain.projection

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate

data class MonthlyEmotionProjection @QueryProjection constructor(
    val date: LocalDate,
    val emotion: String,
)
