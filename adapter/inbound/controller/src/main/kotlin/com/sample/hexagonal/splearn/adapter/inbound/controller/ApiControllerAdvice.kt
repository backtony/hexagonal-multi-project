package com.sample.hexagonal.splearn.adapter.inbound.controller

import com.sample.hexagonal.splearn.domain.member.DuplicateEmailException
import com.sample.hexagonal.splearn.domain.member.DuplicateProfileException
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

    @ExceptionHandler(DuplicateEmailException::class, DuplicateProfileException::class)
    fun duplicateExceptionHandler(exception: RuntimeException): ProblemDetail = getProblemDetail(HttpStatus.CONFLICT, exception)

    private fun getProblemDetail(status: HttpStatus, exception: Exception): ProblemDetail =
        ProblemDetail.forStatusAndDetail(status, exception.message).apply {
            setProperty("timestamp", LocalDateTime.now())
            setProperty("exception", exception.javaClass.simpleName)
        }
}
