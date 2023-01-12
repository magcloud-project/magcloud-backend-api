package co.bearus.magcloud.dto.request

import jakarta.validation.constraints.NotEmpty

data class DiaryPatchDTO(
    @field:NotEmpty(message = "내용은 비어있을 수 없습니다") val content: String? = null,
    val date: String? = null,
    val previousVersion: Long? = null,
    val force: Boolean? = false
)
