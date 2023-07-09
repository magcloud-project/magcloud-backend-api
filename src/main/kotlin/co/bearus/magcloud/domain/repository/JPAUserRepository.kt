package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserRepository : JpaRepository<UserEntity, String> {
    fun findByNameAndTag(name: String, tag: String): UserEntity?
    fun findByEmail(email: String): UserEntity?
}
