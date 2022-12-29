package hackathon.redbeanbackend.entity

import hackathon.redbeanbackend.domain.Gender
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import lombok.AllArgsConstructor

@Entity
@AllArgsConstructor
data class UserEntity(
    @Id @GeneratedValue var id: Long? = null,
    var email: String,
    var password: String,
    var gender: Gender,
    var age: Int,
    var name: String
){
    constructor() : this(null, "", "", Gender.MALE, 0, "")
    constructor(email: String, password: String, gender: Gender, age: Int, name: String) : this()

}
