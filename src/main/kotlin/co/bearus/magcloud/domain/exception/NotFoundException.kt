package co.bearus.magcloud.domain.exception

class NotFoundException(message: String) : DomainException(message) {
    constructor() : this("알 수 없는 오류가 발생했습니다")
}
