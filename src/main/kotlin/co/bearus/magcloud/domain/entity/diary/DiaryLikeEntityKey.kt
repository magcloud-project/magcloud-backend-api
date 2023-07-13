package co.bearus.magcloud.domain.entity.diary

import java.io.Serializable
import java.time.LocalDate

class DiaryLikeEntityKey(
    val diaryId: String = "",
    val userId: String = "",
) : Serializable
