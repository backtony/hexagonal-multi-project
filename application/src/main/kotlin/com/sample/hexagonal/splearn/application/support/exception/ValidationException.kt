package com.sample.hexagonal.splearn.application.support.exception

class ValidationException(val errors: List<String>) : RuntimeException()
