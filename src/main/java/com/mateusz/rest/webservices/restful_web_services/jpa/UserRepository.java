package com.mateusz.rest.webservices.restful_web_services.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mateusz.rest.webservices.restful_web_services.users.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
