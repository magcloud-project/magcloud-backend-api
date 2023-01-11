package co.bearus.magcloud.service.diary

import co.bearus.magcloud.domain.DomainException
import co.bearus.magcloud.domain.Emotion
import co.bearus.magcloud.dto.InferenceRequestDTO
import co.bearus.magcloud.dto.InferenceResponseDTO
import co.bearus.magcloud.entity.diary.UserDiaryEmotionEntity
import co.bearus.magcloud.entity.diary.UserDiaryEntity
import co.bearus.magcloud.repository.JPAUserDiaryEmotionRepository
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
            //val response = _requestInference(diary)
            //Implement Inference Svc
            val selectedTopEmotion = Emotion.values().random()
            Emotion.values().forEach { emotion->
                val data = UserDiaryEmotionEntity(diary, emotion, if(emotion == selectedTopEmotion) 0.6 else 0.1)
                this.userDiaryEmotionRepository.save(data)
            }
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
