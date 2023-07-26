package co.bearus.magcloud.domain.exception

open class DomainException(
    val errorCode: ErrorCode,
) : RuntimeException()

open class NotFoundDomainException : DomainException(
    errorCode = ErrorCode.GENERIC_NOT_FOUND
)
