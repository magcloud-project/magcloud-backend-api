package co.bearus.magcloud.domain.entity.diary

import java.io.Serializable
import java.time.LocalDate

class DiaryEntityKey(
    val userId: String = "",
    val date: LocalDate = LocalDate.now(),
) : Serializable
