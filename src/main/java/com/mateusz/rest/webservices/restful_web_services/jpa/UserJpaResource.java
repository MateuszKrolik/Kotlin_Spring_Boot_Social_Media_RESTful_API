package com.mateusz.rest.webservices.restful_web_services.jpa;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mateusz.rest.webservices.restful_web_services.users.Post;
import com.mateusz.rest.webservices.restful_web_services.users.PostNotFoundException;
import com.mateusz.rest.webservices.restful_web_services.users.User;
import com.mateusz.rest.webservices.restful_web_services.users.UserNotFoundException;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserJpaResource {
    // private UserDaoService userDaoService;

    private UserRepository userRepository;

    private PostRepository postRepository;

    // @Autowired
    public UserJpaResource(UserRepository userRepository,
            PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // GET /users
    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    // http://localhost:8080/users

    // EntityModel
    // WebMvcLinkBuilder

    // GET /users/{id}
    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveOneUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException("id:" + id);
        }
        EntityModel<User> entityModel = EntityModel.of(user.get());

        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());

        entityModel.add(link.withRel("all-users"));

        return entityModel;
    }

    // POST /users
    @PostMapping("/users")
    public ResponseEntity<User> createOneUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);
        // /users/4 => /users/{id}, user.getId
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteOneUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsForUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty())
            throw new UserNotFoundException("id:" + id);

        return user.get().getPosts();
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createOnePostForUser(@PathVariable int id, @Valid @RequestBody Post post) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty())
            throw new UserNotFoundException("id:" + id);

        post.setUser(user.get());// get because it's Optional

        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPost.getId()).toUri();
        return ResponseEntity.created(location).build();

    }

    @GetMapping("/users/{userId}/posts/{postId}")
    public EntityModel<Post> retrieveOnePost(@PathVariable int userId, @PathVariable int postId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("id:" + userId);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("id:" + postId));

        EntityModel<Post> entityModel = EntityModel.of(post);

        WebMvcLinkBuilder linkToUser = linkTo(methodOn(this.getClass()).retrieveOneUser(userId));
        WebMvcLinkBuilder linkToPosts = linkTo(methodOn(this.getClass()).retrieveAllPostsForUser(userId));

        entityModel.add(linkToUser.withRel("user"));
        entityModel.add(linkToPosts.withRel("all-posts"));

        return entityModel;
    }

}
