package com.sample.hexagonal.splearn.application.service.member

import com.sample.hexagonal.splearn.application.service.member.port.inbound.LoginFailedException
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberAuthenticator
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberLoginRequest
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRegister
import com.sample.hexagonal.splearn.application.ApplicationServiceTest
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRequestFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@ApplicationServiceTest
class MemberAuthenticatorTest(
    memberAuthenticator: MemberAuthenticator,
    memberRegister: MemberRegister,
) : StringSpec({
    "login" {
        val registerRequest = MemberRequestFixture.createMemberRegisterRequest()
        val member = memberRegister.register(registerRequest)
        member.activate()

        val loggedInMember = memberAuthenticator.login(MemberLoginRequest(registerRequest.email, registerRequest.password))

        loggedInMember shouldBe member
    }

    "loginFailedNotActive" {
        val registerRequest = MemberRequestFixture.createMemberRegisterRequest()
        memberRegister.register(registerRequest)

        shouldThrow<LoginFailedException> {
            memberAuthenticator.login(MemberLoginRequest(registerRequest.email, registerRequest.password))
        }
    }

    "loginFailedEmailNotExist" {
        val registerRequest = MemberRequestFixture.createMemberRegisterRequest()
        memberRegister.register(registerRequest).activate()

        shouldThrow<LoginFailedException> {
            memberAuthenticator.login(MemberLoginRequest("notexist@email.com", registerRequest.password))
        }
    }

    "loginFailedWrongPassword" {
        val registerRequest = MemberRequestFixture.createMemberRegisterRequest()
        memberRegister.register(registerRequest).activate()

        shouldThrow<LoginFailedException> {
            memberAuthenticator.login(MemberLoginRequest(registerRequest.email, "wrongpassword"))
        }
    }
})
