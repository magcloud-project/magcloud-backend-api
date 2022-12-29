package hackathon.redbeanbackend.entity

import jakarta.persistence.*

@Entity(name="user_tags")
data class UserTagEntity(
    @Id @GeneratedValue var id: Long? = null,
    @ManyToOne @JoinColumn(name = "tag_id") var tagId: TagEntity? = null,
    @ManyToOne @JoinColumn(name = "user_id") var user: UserEntity? = null,
){
    constructor() : this(0,)
    constructor(tagId: TagEntity, user: UserEntity) : this(null, tagId, user)
}
