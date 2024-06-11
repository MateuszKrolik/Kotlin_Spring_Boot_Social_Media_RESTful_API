package com.mateusz.rest.webservices.restful_web_services.jpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.mateusz.rest.webservices.restful_web_services.jpa.PostRepository;
import com.mateusz.rest.webservices.restful_web_services.jpa.UserJpaResource;
import com.mateusz.rest.webservices.restful_web_services.jpa.UserRepository;
import com.mateusz.rest.webservices.restful_web_services.security.SpringSecurityConfiguration;
import com.mateusz.rest.webservices.restful_web_services.users.Post;
import com.mateusz.rest.webservices.restful_web_services.users.User;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserJpaResource.class)
@Import(SpringSecurityConfiguration.class) // to avoid 403 forbidden error
@WithMockUser
public class UserJpaResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper; // to handle LocalDate once and for all

    @Test
    public void testRetrieveAllUsers() throws Exception {
        List<User> mockUsers = List.of(
                new User(1, "Test User 1", LocalDate.of(1990, 1, 1)),
                new User(2, "Test User 2", LocalDate.of(1992, 2, 2)));
        given(userRepository.findAll()).willReturn(mockUsers);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user_name").value("Test User 1"))
                .andExpect(jsonPath("$[1].user_name").value("Test User 2"));
    }

    @Test
    public void testRetrieveOneUser() throws Exception {
        User mockUser = new User(1, "Test User", LocalDate.of(1990, 1, 1));
        given(userRepository.findById(1)).willReturn(Optional.of(mockUser));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name").value("Test User"));
    }

    @Test
    public void testCreateOneUser() throws Exception {
        User newUser = new User(null, "New User", LocalDate.of(1995, 5, 5));
        User savedUser = new User(3, "New User", LocalDate.of(1995, 5, 5));
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/users/3")));
    }

    @Test
    public void testDeleteOneUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 1))
                .andExpect(status().isOk());

        // verify that the repository's deleteById method was called
        verify(userRepository).deleteById(1);
    }

    @Test
    public void testRetrieveAllPostsForUser_UserExists() throws Exception {
        User mockUser = mock(User.class);
        Post post1 = new Post();
        post1.setDescription("I love my job :)");
        Post post2 = new Post();
        post2.setDescription("Another post");
        List<Post> mockPosts = List.of(post1, post2);

        // stubbing methods on the mock objects
        given(userRepository.findById(1)).willReturn(Optional.of(mockUser));
        given(mockUser.getPosts()).willReturn(mockPosts);

        mockMvc.perform(get("/users/{id}/posts", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("I love my job :)"))
                .andExpect(jsonPath("$[1].description").value("Another post"));
    }

    @Test
    public void testRetrieveAllPostsForUser_UserDoesNotExist() throws Exception {
        given(userRepository.findById(anyInt())).willReturn(Optional.empty());

        mockMvc.perform(get("/users/{id}/posts", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOnePostForUser_UserExists() throws Exception {
        User mockUser = new User(1, "Test User", LocalDate.of(1990, 1, 1));
        Post newPost = new Post();
        newPost.setDescription("New Post Description");
        Post savedPost = new Post();
        savedPost.setId(10); // assuming saved post gets ID of 10
        savedPost.setDescription(newPost.getDescription());
        savedPost.setUser(mockUser);

        // stubbing
        given(userRepository.findById(1)).willReturn(Optional.of(mockUser));
        given(postRepository.save(any(Post.class))).willReturn(savedPost);

        mockMvc.perform(post("/users/{id}/posts", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPost)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/users/1/posts/10")));
    }

    @Test
    public void testCreateOnePostForUser_UserDoesNotExist() throws Exception {
        // stub userRepository to return "empty Optional" indicating "user not found"
        given(userRepository.findById(anyInt())).willReturn(Optional.empty());

        Post newPost = new Post();
        newPost.setDescription("New Post Description");

        mockMvc.perform(post("/users/{id}/posts", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPost)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRetrieveOnePost_PostExists() throws Exception {
        User mockUser = new User(1, "Test User", LocalDate.of(1990, 1, 1));
        Post mockPost = new Post(1, "Test Post Description");
        mockPost.setUser(mockUser);

        // Stubbing
        given(userRepository.findById(1)).willReturn(Optional.of(mockUser));
        given(postRepository.findById(1)).willReturn(Optional.of(mockPost));

        mockMvc.perform(get("/users/{userId}/posts/{postId}", 1, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test Post Description"))
                .andExpect(jsonPath("$._links.user.href", containsString("/users/1")))
                .andExpect(jsonPath("$._links.all-posts.href", containsString("/users/1/posts")));
    }

    @Test
    public void testRetrieveOnePost_PostDoesNotExist() throws Exception {
        // stub userRepository to return a valid user
        User mockUser = new User(1, "Test User", LocalDate.of(1990, 1, 1));
        given(userRepository.findById(1)).willReturn(Optional.of(mockUser));

        // stub postRepository to simulate post not found
        given(postRepository.findById(anyInt())).willReturn(Optional.empty());

        mockMvc.perform(get("/users/{userId}/posts/{postId}", 1, 99)) // Assuming 99 is a non-existent post ID
                .andExpect(status().isNotFound());
    }
} // ./mvnw -Dtest=UserJpaResourceTest test