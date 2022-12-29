package hackathon.redbeanbackend.service

import hackathon.redbeanbackend.domain.DomainException
import hackathon.redbeanbackend.domain.NotFoundException
import hackathon.redbeanbackend.dto.APIResponse
import hackathon.redbeanbackend.dto.DiaryResponseDTO
import hackathon.redbeanbackend.dto.InferenceRequestDTO
import hackathon.redbeanbackend.dto.InferenceResponseDTO
import hackathon.redbeanbackend.entity.UserDiaryEntity
import hackathon.redbeanbackend.entity.UserDiaryResultEntity
import hackathon.redbeanbackend.repository.JPAUserDiaryResultRepository
import hackathon.redbeanbackend.repository.JPAUserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class InferenceService(
    private val userDiaryResultRepository: JPAUserDiaryResultRepository,
    @Value("\${secret.inference-url}") val inferenceUrl: String,
    private val translateService: TranslateService) {
    @Async
    fun requestInference(diary: UserDiaryEntity){
        try{
            val response = _requestInference(diary)
            val diaryResult = UserDiaryResultEntity(diary, response.sadness, response.joy, response.natural, response.anger, response.fear, response.depression)
            userDiaryResultRepository.save(diaryResult)
        }catch(ex: RuntimeException){
            ex.printStackTrace()
        }
        //Inference Finished
    }
    private fun _requestInference(diary: UserDiaryEntity): InferenceResponseDTO{
        val translated = translateService.translateToEnglish(diary.content)
        val dto = InferenceRequestDTO(translated)
        return RestTemplate().postForEntity(inferenceUrl, dto, InferenceResponseDTO::class.java).body ?: throw DomainException()
    }
}
