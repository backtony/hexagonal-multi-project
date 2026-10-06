package com.sample.hexagonal.sample.adapter.inbound.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class ApiControllerAdvice : ResponseEntityExceptionHandler() {
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ProblemDetail = getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception)

    private fun getProblemDetail(status: HttpStatus, exception: Exception): ProblemDetail =
        ProblemDetail.forStatusAndDetail(status, exception.message).apply {
            setProperty("timestamp", LocalDateTime.now())
            setProperty("exception", exception.javaClass.simpleName)
        }
}
