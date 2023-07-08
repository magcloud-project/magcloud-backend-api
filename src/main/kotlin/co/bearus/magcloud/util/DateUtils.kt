package co.bearus.magcloud.util

import java.time.LocalDateTime

class DateUtils {
    companion object {
        fun LocalDateTime.toEpochMillis() = this.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
