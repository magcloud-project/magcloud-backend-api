package hackathon.redbeanbackend.entity

import hackathon.redbeanbackend.domain.Gender
import jakarta.persistence.*

@Entity(name = "user")
data class UserEntity(
    @Id @GeneratedValue var id: Long? = null,
    var email: String,
    var password: String,
    @Enumerated(value = EnumType.STRING) var gender: Gender,
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
    constructor() : this(null, "", "", Gender.MALE, 0, "", mutableListOf())
    constructor(email: String, password: String, gender: Gender, age: Int, name: String) : this(
        null,
        email,
        password,
        gender,
        age,
        name
    )

}
