package com.sample.hexagonal.splearn.application

import com.sample.hexagonal.splearn.application.service.member.port.outbound.EmailSender
import com.sample.hexagonal.splearn.domain.member.MemberFixture
import com.sample.hexagonal.splearn.domain.member.PasswordEncoder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class SplearnTestConfiguration {
    @Bean
    fun emailSender(): EmailSender = EmailSender { email, _, _ -> println("Sending email: $email") }

    @Bean
    fun passwordEncoder(): PasswordEncoder = MemberFixture.createPasswordEncoder()
}
