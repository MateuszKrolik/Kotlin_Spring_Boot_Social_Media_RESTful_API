package com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.exception

import com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.users.PostNotFoundException
import com.mateusz.rest.webservices.restful_web_services.spring_boot_social_media_restful_api.users.UserNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime


@ControllerAdvice
class CustomisedResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(Exception::class)
    @Throws(Exception::class)
    fun handleAllException(ex: Exception, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            LocalDateTime.now(), ex.message?: "Unknown error",
            request.getDescription(false)
        )
        return ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(UserNotFoundException::class)
    @Throws(Exception::class)
    fun handleUserNotFoundException(ex: Exception, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            LocalDateTime.now(), ex.message?: "User not found",
            request.getDescription(false)
        )
        return ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(PostNotFoundException::class)
    fun handlePostNotFoundException(
        ex: PostNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            LocalDateTime.now(), ex.message?: "Post not found",
            request.getDescription(false)
        )
        return ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorDetails = ErrorDetails(
            LocalDateTime.now(),
            ("Total errors are: " + ex.errorCount + ", First error is: "
                    + ex.fieldError!!.defaultMessage),
            request.getDescription(false)
        )

        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoSuchElementException::class)
    @Throws(Exception::class)
    fun handleNoSuchElementException(ex: NoSuchElementException, request: WebRequest): ResponseEntity<ErrorDetails> {
        val errorDetails = ErrorDetails(
            LocalDateTime.now(), ex.message ?: "Resource not found",
            request.getDescription(false)
        )
        return ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND)
    }
}
