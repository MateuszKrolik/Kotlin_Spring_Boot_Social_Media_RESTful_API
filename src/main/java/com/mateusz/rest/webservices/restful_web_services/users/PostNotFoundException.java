package com.mateusz.rest.webservices.restful_web_services.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PostNotFoundException extends RuntimeException {// avoid checked exception
    public PostNotFoundException(String message) {
        super(message);// pass msg to runtime exception
    }
}
