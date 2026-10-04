package com.sample.hexagonal.splearn.adapter.outbound.email

import com.sample.hexagonal.splearn.domain.member.Email
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class DummyEmailSenderTest :
    StringSpec({
        "dummyEmailSender" {
            val out = ByteArrayOutputStream()
            val original = System.out
            System.setOut(PrintStream(out))
            try {
                DummyEmailSender().send(Email("toby@splearn.app"), "subject", "body")
            } finally {
                System.setOut(original)
            }

            out.toString().lines().first() shouldBe "DummyEmailSender send email: Email(address=toby@splearn.app)"
        }
    })
