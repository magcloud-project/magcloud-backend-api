package co.bearus.magcloud.domain.exception

class DiaryNotFoundException : DomainException(ErrorCode.DIARY_NOT_FOUND)
class DiaryNotExistsException : NotFoundDomainException()
