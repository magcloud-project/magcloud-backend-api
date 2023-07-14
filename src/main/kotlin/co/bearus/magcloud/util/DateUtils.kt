package co.bearus.magcloud.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtils {
    companion object {
        val simpleYmdFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
        fun LocalDate.toSimpleYmdFormat() = this.format(simpleYmdFormat)
        fun LocalDateTime.toEpochMillis() = this.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        fun dateAtSeoul() = LocalDate.now(java.time.ZoneId.of("Asia/Seoul"))
    }
}
