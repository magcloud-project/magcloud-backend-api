package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.diary.UserDiaryEmotionEntity
import co.bearus.magcloud.domain.entity.diary.UserDiaryEmotionIdentity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserDiaryEmotionRepository : JpaRepository<UserDiaryEmotionEntity, UserDiaryEmotionIdentity> {

}
