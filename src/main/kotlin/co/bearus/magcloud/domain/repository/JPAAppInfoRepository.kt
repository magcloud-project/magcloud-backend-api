package co.bearus.magcloud.domain.repository

import co.bearus.magcloud.domain.entity.AppInfoEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JPAAppInfoRepository : JpaRepository<AppInfoEntity, String>
