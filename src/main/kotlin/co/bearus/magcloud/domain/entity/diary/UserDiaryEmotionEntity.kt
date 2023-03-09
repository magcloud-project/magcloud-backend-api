package co.bearus.magcloud.domain.entity.diary

import co.bearus.magcloud.domain.type.Emotion
import jakarta.persistence.*

@Entity(name = "user_diary_emotion")
@IdClass(UserDiaryEmotionIdentity::class)
data class UserDiaryEmotionEntity(
    @Id
    @ManyToOne
    @JoinColumn(name = "diary_id")
    var diary: UserDiaryEntity? = null,

    @Id
    @Enumerated(value = EnumType.STRING)
    var emotion: Emotion,

    @Column(name = "value")
    var value: Double
) {

    override fun hashCode(): Int {
        return emotion.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserDiaryEmotionEntity

        if (emotion != other.emotion) return false

        return true
    }
}
