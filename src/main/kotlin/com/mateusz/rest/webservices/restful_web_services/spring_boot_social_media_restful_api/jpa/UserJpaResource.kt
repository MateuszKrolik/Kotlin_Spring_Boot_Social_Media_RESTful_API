package com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.jpa

import com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.users.Post
import com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.users.PostNotFoundException
import com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.users.User
import com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.users.UserNotFoundException
import jakarta.validation.Valid
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*


@RestController
class UserJpaResource(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) {

    // GET /users
    @GetMapping(value = ["/users"], produces = ["application/json", "application/xml", "application/hal+json"] )
    fun retrieveAllUsers(): List<User> {
        return userRepository.findAll().filterNotNull()
    }

    // GET /users/{id}
    @GetMapping(value = ["/users/{id}"], produces = ["application/json", "application/xml", "application/hal+json"] )
    fun retrieveOneUser(@PathVariable id: Int): EntityModel<User> {
        val user = userRepository.findById(id).orElseThrow { UserNotFoundException("User not found with id: $id") }

        val entityModel = EntityModel.of(user!!)

        val link: WebMvcLinkBuilder = linkTo(methodOn(this.javaClass).retrieveAllUsers())

        entityModel.add(link.withRel("all-users"))

        return entityModel
    }

    // POST /users
    @PostMapping(value = ["/users"], consumes = ["application/json", "application/xml", "application/hal+json"], produces = ["application/json", "application/xml", "application/hal+json"] )
    fun createOneUser(@Valid @RequestBody user: User?): ResponseEntity<User> {
        val savedUser: User = userRepository.save(user!!)
        val location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(savedUser.id).toUri()
        return ResponseEntity.created(location).build()
    }

    @DeleteMapping(value = ["/users/{id}"], produces = ["application/json", "application/xml", "application/hal+json"])
    fun deleteOneUser(@PathVariable id: Int) {
        userRepository.deleteById(id)
    }

    @GetMapping(value = ["/users/{id}/posts"], produces = ["application/json", "application/xml", "application/hal+json"] )
    fun retrieveAllPostsForUser(@PathVariable id: Int): List<Post> {
        val user = userRepository.findById(id).orElseThrow { UserNotFoundException("User not found with id: $id") }
        return user?.posts ?: emptyList()
    }

    @PostMapping(value = ["/users/{id}/posts"], consumes = ["application/json", "application/xml", "application/hal+json"] )
    fun createOnePostForUser(@PathVariable id: Int, @Valid @RequestBody post: Post): ResponseEntity<Post> {
        val user: Optional<User> = userRepository.findById(id).map { it ?: throw UserNotFoundException("id:$id") }
        post.user = user.get() // get because it's Optional

        val savedPost: Post = postRepository.save(post)

        val location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(savedPost.id).toUri()
        return ResponseEntity.created(location).build()
    }

    @GetMapping(value = ["/users/{userId}/posts/{postId}"], produces = ["application/json", "application/xml", "application/hal+json"] )
    fun retrieveOnePost(@PathVariable userId: Int, @PathVariable postId: Int): EntityModel<Post> {
        userRepository.findById(userId).orElseThrow { UserNotFoundException("User not found with id: $userId") }
        val post = postRepository.findById(postId).orElseThrow { PostNotFoundException("Post not found with id: $postId") }

        val entityModel = EntityModel.of(post!!)

        val linkToUser: WebMvcLinkBuilder = linkTo(methodOn(this.javaClass).retrieveOneUser(userId))
        val linkToPosts: WebMvcLinkBuilder = linkTo(methodOn(this.javaClass).retrieveAllPostsForUser(userId))

        entityModel.add(linkToUser.withRel("user"))
        entityModel.add(linkToPosts.withRel("all-posts"))

        return entityModel
    }
}