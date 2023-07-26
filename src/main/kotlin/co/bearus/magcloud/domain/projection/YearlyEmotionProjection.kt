package co.bearus.magcloud.domain.projection

import com.querydsl.core.annotations.QueryProjection

data class YearlyEmotionProjection @QueryProjection constructor(
    val month: Int,
    val emotion: String,
    val count: Long,
)
