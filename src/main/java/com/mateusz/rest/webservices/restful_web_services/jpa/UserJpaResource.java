package com.mateusz.rest.webservices.restful_web_services.jpa;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mateusz.rest.webservices.restful_web_services.users.User;
// import com.mateusz.rest.webservices.restful_web_services.users.UserDaoService;
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

    // @Autowired
    public UserJpaResource(UserRepository userRepository
    // UserDaoService userDaoService
    ) {
        // this.userDaoService = userDaoService;
        this.userRepository = userRepository;
    }

    // GET /users
    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    // http://localhost:8080/users

    // EntityModel
    // WebMvcLinkBuilder

    // GET /users/{id}
    @GetMapping("/jpa/users/{id}")
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
    @PostMapping("/jpa/users")
    public ResponseEntity<User> createOneUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);
        // /users/4 => /users/{id}, user.getId
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/jpa/users/{id}")
    public void deleteOneUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }

}
