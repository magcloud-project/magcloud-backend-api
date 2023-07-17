package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.user.UserSocialEntity
import co.bearus.magcloud.domain.entity.user.UserSocialEntityKey
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserSocialRepository : JpaRepository<UserSocialEntity, UserSocialEntityKey> {
    fun deleteAllByUserId(userId: String)
}
