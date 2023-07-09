package co.bearus.magcloud.domain.exception

open class DomainException(
    val errorCode: ErrorCode
) : RuntimeException()
