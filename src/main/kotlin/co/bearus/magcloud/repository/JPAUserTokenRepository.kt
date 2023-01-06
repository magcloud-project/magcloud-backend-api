package co.bearus.magcloud.repository

import co.bearus.magcloud.entity.UserTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserTokenRepository : JpaRepository<UserTokenEntity, Long>