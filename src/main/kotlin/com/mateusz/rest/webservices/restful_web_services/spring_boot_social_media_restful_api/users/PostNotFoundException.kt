package com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.users

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(code = HttpStatus.NOT_FOUND)
class PostNotFoundException  // avoid checked exception
    (message: String?) : RuntimeException(message)
