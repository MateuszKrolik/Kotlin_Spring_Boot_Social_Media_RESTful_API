package com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.users

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
class Post {
    @Id
    @GeneratedValue
    var id: Int? = null

    @Size(min = 10)
    var description: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    var user: User? = null

    constructor()

    constructor(id: Int?, description: String?) {
        this.id = id
        this.description = description
    }

    override fun toString(): String {
        return "Post [id=$id, description=$description]"
    }
}
