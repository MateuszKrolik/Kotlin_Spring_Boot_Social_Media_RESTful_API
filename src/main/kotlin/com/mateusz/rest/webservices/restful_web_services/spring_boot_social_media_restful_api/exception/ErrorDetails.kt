package com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.exception

import java.time.LocalDateTime

data class ErrorDetails(
    val timeStamp: LocalDateTime,
    val message: String,
    val details: String
)