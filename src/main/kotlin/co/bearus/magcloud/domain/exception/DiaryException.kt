package co.bearus.magcloud.domain.exception

class DiaryNotFoundException : DomainException(ErrorCode.DIARY_NOT_FOUND)
class DiaryNotExistsException : NotFoundDomainException()

class DiaryTooOldException : DomainException(ErrorCode.CANNOT_UPDATE_MORE_THAN_TWO_DAYS)
