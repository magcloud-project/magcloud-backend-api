package co.bearus.magcloud.domain.service

import co.bearus.magcloud.domain.repository.JPAAppInfoRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class AppInfoService(
    private val jpaAppInfoRepository: JPAAppInfoRepository,
) {
    @PostConstruct
    fun postConstruct() {
        updateAppInfoCache()
    }

    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 60)
    fun updateAppInfoCache() {
        jpaAppInfoRepository.findAll()
            .forEach {
                appInfoCache[it.appKey] = AppInfo(
                    appVersion = it.appVersion,
                    inService = it.inService,
                    inReview = it.inReview,
                )
            }
    }

    fun getAppInfoByKey(appKey: String) = appInfoCache[appKey]

    companion object {
        private val appInfoCache = mutableMapOf<String, AppInfo>()
    }

    data class AppInfo(
        val appVersion: String,
        val inService: Boolean,
        val inReview: Boolean,
    )
}
