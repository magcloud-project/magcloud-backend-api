package co.bearus.magcloud.domain.entity

import co.bearus.magcloud.domain.entity.user.UserEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "user_tags")
data class UserTagEntity(
    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id")
    var tagId: TagEntity? = null,

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity? = null,
)
