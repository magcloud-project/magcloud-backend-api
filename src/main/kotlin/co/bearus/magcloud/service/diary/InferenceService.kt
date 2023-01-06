package co.bearus.magcloud.service.diary

import co.bearus.magcloud.domain.DomainException
import co.bearus.magcloud.dto.InferenceRequestDTO
import co.bearus.magcloud.dto.InferenceResponseDTO
import co.bearus.magcloud.entity.UserDiaryEntity
import co.bearus.magcloud.entity.UserDiaryResultEntity
import co.bearus.magcloud.repository.JPAUserDiaryResultRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class InferenceService(
    private val userDiaryResultRepository: JPAUserDiaryResultRepository,
    @Value("\${secret.inference-url}") val inferenceUrl: String,
    private val translateService: TranslateService
) {
    @Async
    fun requestInference(diary: UserDiaryEntity) {
        try {
            val response = _requestInference(diary)

            val previousInference = userDiaryResultRepository.findByDiaryId(diary.id!!)
            if (previousInference != null) {
                previousInference.anxious = response.Anxious;
                previousInference.lonely = response.Lonely;
                previousInference.normal = response.Normal;
                previousInference.stress = response.Stressed;

                userDiaryResultRepository.save(previousInference)
            } else {
                val diaryResult =
                    UserDiaryResultEntity(diary, response.Stressed, response.Anxious, response.Normal, response.Lonely)
                userDiaryResultRepository.save(diaryResult)
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
