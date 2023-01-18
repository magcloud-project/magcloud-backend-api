package co.bearus.magcloud.repository

import co.bearus.magcloud.entity.diary.UserDiaryEmotionEntity
import co.bearus.magcloud.entity.diary.UserDiaryEmotionIdentity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserDiaryEmotionRepository : JpaRepository<UserDiaryEmotionEntity, UserDiaryEmotionIdentity> {

}
