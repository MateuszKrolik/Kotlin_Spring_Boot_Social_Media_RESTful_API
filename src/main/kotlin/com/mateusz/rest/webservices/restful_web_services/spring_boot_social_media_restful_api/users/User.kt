package com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.users

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import java.time.LocalDate

@Entity(name = "user_details")
class User {
    @Id
    @GeneratedValue
    var id: Int? = null

    @Size(min = 2, message = "Name should have at least 2 characters!")
    @JsonProperty("user_name")
    var name: String? = null

    @JsonProperty("birth_date")
    @Past(message = "BirthDate should be in the past!")
    var birthDate: LocalDate? = null

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    var posts: List<Post>? = null

    constructor(id: Int?, name: String?, birthDate: LocalDate?) {
        this.id = id
        this.name = name
        this.birthDate = birthDate
    }

    protected constructor()

    override fun toString(): String {
        return "User [id=$id, name=$name, birthDate=$birthDate]"
    }
}
