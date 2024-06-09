package com.mateusz.rest.webservices.restful_web_services.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mateusz.rest.webservices.restful_web_services.users.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
