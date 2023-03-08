package co.bearus.magcloud.domain.service.diary

import co.bearus.magcloud.domain.exception.DomainException
import co.bearus.magcloud.domain.type.Emotion
import co.bearus.magcloud.domain.entity.diary.UserDiaryEmotionEntity
import co.bearus.magcloud.domain.entity.diary.UserDiaryEntity
import co.bearus.magcloud.domain.repository.JPAUserDiaryEmotionRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class InferenceService(
    @Value("\${secret.inference-url}") val inferenceUrl: String,
    private val translateService: TranslateService,
    private val userDiaryEmotionRepository: JPAUserDiaryEmotionRepository
) {
    @Async
    fun requestInference(diary: UserDiaryEntity) {
        try {
            val selectedTopEmotion = Emotion.values().random()
            Emotion.values().forEach { emotion ->
                val data = UserDiaryEmotionEntity(diary, emotion, if (emotion == selectedTopEmotion) 0.6 else 0.1)
                this.userDiaryEmotionRepository.save(data)
            }
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
        }
    }

    private fun _requestInference(diary: UserDiaryEntity): co.bearus.magcloud.controller.dto.InferenceResponseDTO {
        val translated = translateService.translateToEnglish(diary.content)
        val dto = co.bearus.magcloud.controller.dto.InferenceRequestDTO(translated)
        return RestTemplate().postForEntity(
            inferenceUrl,
            dto,
            co.bearus.magcloud.controller.dto.InferenceResponseDTO::class.java
        ).body
            ?: throw DomainException()
    }
}
