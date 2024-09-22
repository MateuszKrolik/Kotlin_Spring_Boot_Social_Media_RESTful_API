package com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.jpa

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.util.*

import java.util.Optional

import org.springframework.security.test.context.support.WithMockUser
import com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.security.SpringSecurityConfiguration
import com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.users.Post
import com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.users.User

@WebMvcTest(UserJpaResource::class)
@Import(SpringSecurityConfiguration::class) // to avoid 403 forbidden error
@WithMockUser
@Suppress("UNCHECKED_CAST")
class UserJpaResourceTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @Test
    @Throws(Exception::class)
    fun testRetrieveAllUsers() {
        val mockUsers: List<User> = listOf(
            User(1, "Test User 1", LocalDate.of(1990, 1, 1)),
            User(2, "Test User 2", LocalDate.of(1992, 2, 2))
        )
        BDDMockito.given(userRepository.findAll()).willReturn(mockUsers)

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].user_name").value("Test User 1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].user_name").value("Test User 2"))
    }
    

    @Test
    @Throws(Exception::class)
    fun testRetrieveOneUser() {
        val mockUser = User(1, "Test User", LocalDate.of(1990, 1, 1))
        BDDMockito.given(userRepository.findById(1)).willReturn(Optional.of(mockUser) as Optional<User?>)

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.user_name").value("Test User"))
    }

    @Test
    @Throws(Exception::class)
    fun testCreateOneUser() {
        val newUser = User(null, "New User", LocalDate.of(1995, 5, 5))
        val savedUser = User(3, "New User", LocalDate.of(1995, 5, 5))
        BDDMockito.given(userRepository.save(ArgumentMatchers.any(User::class.java))).willReturn(savedUser)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.containsString("/users/3")))
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteOneUser() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 1))
            .andExpect(MockMvcResultMatchers.status().isOk())

        // verify that the repository's deleteById method was called
        Mockito.verify(userRepository)?.deleteById(1)
    }

    @Test
    @Throws(Exception::class)
    fun testRetrieveAllPostsForUser_UserExists() {
        val mockUser: User = Mockito.mock(User::class.java)
        val post1 = Post()
        post1.description = "I love my job :)"
        val post2 = Post()
        post2.description = "Another post"
        val mockPosts: List<Post> = listOf(post1, post2)

        // stubbing methods on the mock objects
        BDDMockito.given(userRepository.findById(1)).willReturn(Optional.of(mockUser) as Optional<User?>)
        BDDMockito.given(mockUser.posts).willReturn(mockPosts)

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}/posts", 1))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("I love my job :)"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Another post"))
    }

    @Test
    @Throws(Exception::class)
    fun testRetrieveAllPostsForUser_UserDoesNotExist() {
        BDDMockito.given(userRepository.findById(ArgumentMatchers.anyInt())).willReturn(Optional.empty<User>() as Optional<User?>)

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}/posts", 1))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    @Test
    @Throws(Exception::class)
    fun testCreateOnePostForUser_UserExists() {
        val mockUser = User(1, "Test User", LocalDate.of(1990, 1, 1))
        val newPost = Post()
        newPost.description = "New Post Description"
        val savedPost = Post()
        savedPost.id = 10
        savedPost.description = newPost.description
        savedPost.user = mockUser

        // stubbing
        BDDMockito.given(userRepository.findById(1)).willReturn(Optional.of(mockUser) as Optional<User?>)
        BDDMockito.given(postRepository.save(ArgumentMatchers.any(Post::class.java))).willReturn(savedPost)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/{id}/posts", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPost))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.containsString("/users/1/posts/10")))
    }

    @Test
    @Throws(Exception::class)
    fun testCreateOnePostForUser_UserDoesNotExist() {
        // stub userRepository to return "empty Optional" indicating "user not found"
        val emptyOptional: Optional<User> = Optional.empty()
        BDDMockito.given(userRepository.findById(ArgumentMatchers.anyInt())).willReturn(emptyOptional as Optional<User?>)

        val newPost = Post()
        newPost.description = "New Post Description"

        mockMvc.perform(
            MockMvcRequestBuilders.post("/users/{id}/posts", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPost))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    @Test
    @Throws(Exception::class)
    fun testRetrieveOnePost_PostExists() {
        val mockUser = User(1, "Test User", LocalDate.of(1990, 1, 1))
        val mockPost = Post(1, "Test Post Description")
        mockPost.user = mockUser

        // Stubbing
        BDDMockito.given(userRepository.findById(1)).willReturn(Optional.of(mockUser) as Optional<User?>)
        BDDMockito.given(postRepository.findById(1)).willReturn(Optional.of(mockPost) as Optional<Post?>)

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/posts/{postId}", 1, 1))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Post Description"))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.user.href", Matchers.containsString("/users/1")))
            .andExpect(
                MockMvcResultMatchers.jsonPath(
                    "$._links.all-posts.href",
                    Matchers.containsString("/users/1/posts")
                )
            )
    }

    @Test
    @Throws(Exception::class)
    fun testRetrieveOnePost_PostDoesNotExist() {
        // stub userRepository to return a valid user
        val mockUser = User(1, "Test User", LocalDate.of(1990, 1, 1))
        

        BDDMockito.given(userRepository.findById(1)).willReturn(Optional.of(mockUser) as Optional<User?>)

        // stub postRepository to simulate post not found
        BDDMockito.given(postRepository.findById(ArgumentMatchers.anyInt())).willReturn(Optional.empty<Post>() as Optional<Post?>)

        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/users/{userId}/posts/{postId}",
                1,
                99
            )
        ) // assuming 99 is a non-existent post ID
            .andExpect(MockMvcResultMatchers.status().isNotFound())
    }
}