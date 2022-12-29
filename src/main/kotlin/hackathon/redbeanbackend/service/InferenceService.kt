package hackathon.redbeanbackend.service

import hackathon.redbeanbackend.domain.DomainException
import hackathon.redbeanbackend.dto.InferenceRequestDTO
import hackathon.redbeanbackend.dto.InferenceResponseDTO
import hackathon.redbeanbackend.entity.UserDiaryEntity
import hackathon.redbeanbackend.entity.UserDiaryResultEntity
import hackathon.redbeanbackend.repository.JPAUserDiaryResultRepository
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
        println("inference start");
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
        //Inference Finished
        println("inference fin");
    }

    private fun _requestInference(diary: UserDiaryEntity): InferenceResponseDTO {
        val translated = translateService.translateToEnglish(diary.content)
        val dto = InferenceRequestDTO(translated)
        return RestTemplate().postForEntity(inferenceUrl, dto, InferenceResponseDTO::class.java).body
            ?: throw DomainException()
    }
}
