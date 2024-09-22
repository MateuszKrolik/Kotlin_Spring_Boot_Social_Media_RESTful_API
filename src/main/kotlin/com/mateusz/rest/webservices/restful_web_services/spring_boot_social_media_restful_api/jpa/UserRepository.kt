package com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.jpa

import com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.users.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User?, Int?>
