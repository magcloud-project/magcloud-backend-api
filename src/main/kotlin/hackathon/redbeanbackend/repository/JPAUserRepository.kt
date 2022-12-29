package hackathon.redbeanbackend.repository

import hackathon.redbeanbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserRepository : JpaRepository<UserEntity, Long> {
    fun getByEmail(email: String): UserEntity?
}
