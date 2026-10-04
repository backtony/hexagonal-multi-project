package com.sample.hexagonal.splearn.domain.member

interface PasswordEncoder {
    fun encode(password: String): String

    fun matches(password: String, passwordHash: String): Boolean
}
