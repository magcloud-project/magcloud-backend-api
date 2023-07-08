package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.user.UserTokenEntity
import co.bearus.magcloud.domain.entity.user.UserTokenEntityKey
import org.springframework.data.jpa.repository.JpaRepository

interface JPAUserTokenRepository : JpaRepository<UserTokenEntity, UserTokenEntityKey>
