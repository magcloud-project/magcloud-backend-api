package co.bearus.magcloud.service.diary

import co.bearus.magcloud.domain.DomainException
import co.bearus.magcloud.dto.InferenceRequestDTO
import co.bearus.magcloud.dto.InferenceResponseDTO
import co.bearus.magcloud.entity.diary.UserDiaryEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class InferenceService(
    @Value("\${secret.inference-url}") val inferenceUrl: String,
    private val translateService: TranslateService
) {
    @Async
    fun requestInference(diary: UserDiaryEntity) {
        try {
            val response = _requestInference(diary)
            //Implement Inference Svc
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
        }
    }

    private fun _requestInference(diary: UserDiaryEntity): InferenceResponseDTO {
        val translated = translateService.translateToEnglish(diary.content)
        val dto = InferenceRequestDTO(translated)
        return RestTemplate().postForEntity(inferenceUrl, dto, InferenceResponseDTO::class.java).body
            ?: throw DomainException()
    }
}
