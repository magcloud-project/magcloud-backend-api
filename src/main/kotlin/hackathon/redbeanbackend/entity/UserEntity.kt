package hackathon.redbeanbackend.entity

import jakarta.persistence.*

@Entity(name = "user")
data class UserEntity(
    @Id @GeneratedValue var id: Long? = null,
    var email: String,
    var password: String,
    var age: Int,
    var name: String,
    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    ) var tags: MutableList<UserTagEntity> = mutableListOf(),
    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    ) var diaries: MutableList<UserDiaryEntity> = mutableListOf()
) {
    constructor() : this(null, "", "", 0, "", mutableListOf())
    constructor(email: String, password: String, age: Int, name: String) : this(
        null,
        email,
        password,
        age,
        name
    )

}
