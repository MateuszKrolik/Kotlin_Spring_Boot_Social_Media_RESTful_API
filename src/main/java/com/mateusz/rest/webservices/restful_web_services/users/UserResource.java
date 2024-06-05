package com.mateusz.rest.webservices.restful_web_services.users;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        return userDaoService.findOne(id);
    }

    // POST /users
    @PostMapping("/users")
    public void createOneUser(@RequestBody User user) {
        userDaoService.save(user);
    }

}
