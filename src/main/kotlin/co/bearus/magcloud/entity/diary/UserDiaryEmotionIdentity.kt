package co.bearus.magcloud.entity.diary

import co.bearus.magcloud.domain.Emotion
import java.io.Serializable

class UserDiaryEmotionIdentity(val diary: UserDiaryEntity, val emotion: Emotion) : Serializable {
    constructor() : this(UserDiaryEntity(), Emotion.NEUTRAL)
}
