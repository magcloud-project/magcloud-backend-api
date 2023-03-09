package co.bearus.magcloud.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "tags")
data class TagEntity(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column(name = "name")
    var name: String,
)
