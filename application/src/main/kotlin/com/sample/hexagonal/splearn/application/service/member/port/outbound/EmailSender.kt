package com.sample.hexagonal.splearn.application.service.member.port.outbound

import com.sample.hexagonal.splearn.domain.member.Email

/**
 * 이메일을 발송한다
 */
fun interface EmailSender {
    fun send(email: Email, subject: String, body: String)
}
