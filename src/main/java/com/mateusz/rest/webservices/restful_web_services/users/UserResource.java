package com.mateusz.rest.webservices.restful_web_services.users;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserResource {
    private UserDaoService userDaoService;

    // @Autowired
    public UserResource(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    // GET /users
    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userDaoService.findAll();
    }

    // GET /users/{id}
    @GetMapping("/users/{id}")
    public User retrieveOneUser(@PathVariable int id) {
        User user = userDaoService.findOne(id);

        if (user == null) {
            throw new UserNotFoundException("id:" + id);
        }
        return user;
    }

    // POST /users
    @PostMapping("/users")
    public ResponseEntity<User> createOneUser(@RequestBody User user) {
        User savedUser = userDaoService.save(user);
        // /users/4 => /users/{id}, user.getId
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteOneUser(@PathVariable int id) {
        userDaoService.deleteById(id);
    }

}
