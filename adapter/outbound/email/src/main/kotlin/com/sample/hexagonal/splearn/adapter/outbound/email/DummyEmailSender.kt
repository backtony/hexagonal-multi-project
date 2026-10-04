package com.sample.hexagonal.splearn.adapter.outbound.email

import com.sample.hexagonal.splearn.application.service.member.port.outbound.EmailSender
import com.sample.hexagonal.splearn.domain.member.Email
import org.springframework.context.annotation.Fallback
import org.springframework.stereotype.Component

@Component
@Fallback
class DummyEmailSender : EmailSender {
    override fun send(email: Email, subject: String, body: String) {
        println("DummyEmailSender send email: $email")
    }
}
