package hackathon.redbeanbackend.repository

import hackathon.redbeanbackend.entity.UserTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserTokenRepository : JpaRepository<UserTokenEntity, Long>
