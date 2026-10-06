package com.sample.hexagonal.sample.application.support.exception

class ValidationException(val errors: List<String>) : RuntimeException()
