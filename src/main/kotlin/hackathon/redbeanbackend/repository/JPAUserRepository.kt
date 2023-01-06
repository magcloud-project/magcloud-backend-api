package hackathon.redbeanbackend.repository

import hackathon.redbeanbackend.domain.LoginProvider
import hackathon.redbeanbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserRepository : JpaRepository<UserEntity, Long> {
    fun getByEmail(email: String): UserEntity?
    fun getByProviderAndEmail(provider: LoginProvider, email: String): UserEntity?
    fun getByProviderAndUserIdentifier(provider: LoginProvider, userIdentifier: String): UserEntity?
}
