package co.bearus.magcloud.domain.service.diary

//@Service
//class InferenceService(
//    @Value("\${secret.inference-url}") val inferenceUrl: String,
//    private val translateService: TranslateService,
//    private val userDiaryEmotionRepository: JPAUserDiaryEmotionRepository
//) {
//    @Async
//    fun requestInference(diary: UserDiaryEntity) {
//        try {
//            val selectedTopEmotion = Emotion.values().random()
//            Emotion.values().forEach { emotion ->
//                val data = UserDiaryEmotionEntity(diary, emotion, if (emotion == selectedTopEmotion) 0.6 else 0.1)
//                this.userDiaryEmotionRepository.save(data)
//            }
//        } catch (ex: RuntimeException) {
//            ex.printStackTrace()
//        }
//    }
//
//    private fun _requestInference(diary: UserDiaryEntity): co.bearus.magcloud.controller.dto.InferenceResponseDTO {
//        val translated = translateService.translateToEnglish(diary.content)
//        val dto = co.bearus.magcloud.controller.dto.InferenceRequestDTO(translated)
//        return RestTemplate().postForEntity(
//            inferenceUrl,
//            dto,
//            co.bearus.magcloud.controller.dto.InferenceResponseDTO::class.java
//        ).body
//            ?: throw DomainException()
//    }
//}
