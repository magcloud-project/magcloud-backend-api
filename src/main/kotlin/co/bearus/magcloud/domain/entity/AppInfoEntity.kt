package co.bearus.magcloud.domain.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "app_info")
class AppInfoEntity private constructor(
    @Id
    @Column(name = "app_key")
    val appKey: String,

    @Column(name = "app_name")
    var appName: String,

    @Column(name = "app_version")
    var appVersion: String,

    @Column(name = "in_service")
    var inService: Boolean,

    @Column(name = "in_review")
    var inReview: Boolean,
) : Serializable, BaseAuditEntity()
