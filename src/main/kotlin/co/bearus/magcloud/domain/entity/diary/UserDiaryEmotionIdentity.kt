package co.bearus.magcloud.domain.entity.diary

import co.bearus.magcloud.domain.type.Emotion
import java.io.Serializable

class UserDiaryEmotionIdentity(
    val diary: UserDiaryEntity,
    val emotion: Emotion
) : Serializable
