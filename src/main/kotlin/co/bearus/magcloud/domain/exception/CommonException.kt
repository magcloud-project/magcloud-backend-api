package co.bearus.magcloud.domain.exception

class UnAuthenticatedException : DomainException(ErrorCode.UNAUTHENTICATED)
class UnAuthorizedException : DomainException(ErrorCode.UNAUTHORIZED)
class ValidationException : DomainException(ErrorCode.VALIDATION_EXCEPTION)
class IntegrityViolationException : DomainException(ErrorCode.INTEGRITY_VIOLATION)
class TokenExpiredException : DomainException(ErrorCode.TOKEN_EXPIRED)
class AuthFailedException : DomainException(ErrorCode.AUTH_FAILED)
class AppVersionException : DomainException(ErrorCode.APP_VERSION_EXCEPTION)
