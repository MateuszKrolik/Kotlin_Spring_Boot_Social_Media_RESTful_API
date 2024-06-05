package com.mateusz.rest.webservices.restful_web_services.users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserDaoService {
    // SpringDataJpa/Hibernate > DB
    // UserDaoService > Static List

    private static List<User> users = new ArrayList<>();

    static {
        users.add(new User(1, "Adam", LocalDate.now().minusYears(20)));
        users.add(new User(2, "Eve", LocalDate.now().minusYears(25)));
        users.add(new User(3, "Jim", LocalDate.now().minusYears(30)));

    }

    public List<User> findAll() {
        return users;
    }

    // public User save(User user);

    // public User findOne(User user);

}
