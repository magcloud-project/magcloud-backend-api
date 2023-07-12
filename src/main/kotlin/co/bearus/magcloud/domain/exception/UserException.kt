package co.bearus.magcloud.domain.exception

class UserNotFoundException : DomainException(ErrorCode.USER_NOT_FOUND)
class UserNameTooLongException : DomainException(ErrorCode.USER_NICKNAME_TOO_LONG)
