package co.bearus.magcloud.dto.request

import java.time.LocalDate

data class UpdateRequestDTO(val date: LocalDate, val contentHash: String)
