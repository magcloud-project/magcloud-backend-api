package co.bearus.magcloud.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import lombok.AllArgsConstructor

@Entity(name = "tags")
@AllArgsConstructor
data class TagEntity(
    @Id @GeneratedValue var id: Long? = null,
    var name: String
) {
    constructor() : this(null, "")
    constructor(name: String) : this(null, name)
}
