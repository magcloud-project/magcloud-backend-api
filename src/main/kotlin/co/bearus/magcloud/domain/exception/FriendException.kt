package co.bearus.magcloud.domain.exception

class AlreadyFriendException : DomainException(ErrorCode.ALREADY_FRIEND)
class AlreadyFriendRequestedException : DomainException(ErrorCode.ALREADY_FRIEND_REQUESTED)
class FriendRequestNotFoundException : DomainException(ErrorCode.FRIEND_REQUEST_NOT_FOUND)
class NotFriendException : DomainException(ErrorCode.NOT_FRIEND)
class CannotRequestToMyselfException : DomainException(ErrorCode.CANNOT_REQUEST_TO_MYSELF)
