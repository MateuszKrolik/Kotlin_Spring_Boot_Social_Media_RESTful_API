package com.mateusz.rest.webservices.restful_web_services.users;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

}
