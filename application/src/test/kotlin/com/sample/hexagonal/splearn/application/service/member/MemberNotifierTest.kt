package com.sample.hexagonal.splearn.application.service.member

import com.sample.hexagonal.splearn.application.service.member.port.outbound.EmailSender
import com.sample.hexagonal.splearn.application.service.member.port.outbound.MemberRepository
import com.sample.hexagonal.splearn.domain.member.MemberFixture
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class MemberNotifierTest :
    StringSpec({
        "sendWelcomeEmail" {
            val member = MemberFixture.createMember()
            val memberRepository = mockk<MemberRepository> { every { findById(1L) } returns member }
            val emailSender = mockk<EmailSender>(relaxed = true)

            MemberNotificationService(memberRepository, emailSender).sendWelcomeEmail(1L)

            verify { emailSender.send(member.email, any(), any()) }
        }
    })
