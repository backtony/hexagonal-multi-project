package com.sample.hexagonal.splearn.domain.member

import com.sample.hexagonal.splearn.domain.member.MemberFixture.createMemberRegisterInfo
import com.sample.hexagonal.splearn.domain.member.MemberFixture.createPasswordEncoder
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class MemberTest :
    StringSpec({
        lateinit var member: Member
        lateinit var passwordEncoder: PasswordEncoder
        lateinit var registerInfo: MemberRegisterInfo

        beforeTest {
            passwordEncoder = createPasswordEncoder()
            registerInfo = createMemberRegisterInfo()
            member = Member.register(registerInfo, passwordEncoder)
        }

        "registerMember" {
            member.status shouldBe MemberStatus.PENDING
            member.detail.createdAt.shouldNotBeNull()
            member.updatedAt shouldBe member.createdAt
        }

        "activate" {
            member.detail.activatedAt.shouldBeNull()

            member.activate()

            member.status shouldBe MemberStatus.ACTIVE
            member.detail.activatedAt.shouldNotBeNull()
            (member.updatedAt >= member.createdAt) shouldBe true
        }

        "activateFail" {
            member.activate()

            shouldThrow<IllegalStateException> { member.activate() }
        }

        "deactivate" {
            member.activate()

            member.deactivate()

            member.status shouldBe MemberStatus.DEACTIVATED
            member.detail.deactivatedAt.shouldNotBeNull()
        }

        "deactivateFail" {
            shouldThrow<IllegalStateException> { member.deactivate() }

            member.activate()
            member.deactivate()

            shouldThrow<IllegalStateException> { member.deactivate() }
        }

        "verifyPassword" {
            member.verifyPassword(registerInfo.password, passwordEncoder) shouldBe true
            member.verifyPassword("hello", passwordEncoder) shouldBe false
        }

        "changePassword" {
            member.changePassword("verysecret2", passwordEncoder)

            member.verifyPassword("verysecret2", passwordEncoder) shouldBe true
        }

        "isActive" {
            member.isActive() shouldBe false

            member.activate()

            member.isActive() shouldBe true

            member.deactivate()

            member.isActive() shouldBe false
        }

        "invalidEmail" {
            shouldThrow<IllegalArgumentException> {
                Member.register(createMemberRegisterInfo("invalid email"), passwordEncoder)
            }

            Member.register(createMemberRegisterInfo(), passwordEncoder)
        }

        "updateInfo" {
            member.activate()

            val request = MemberInfoUpdate("Leo", "toby100", "자기소개")
            member.updateInfo(request)

            member.nickname shouldBe request.nickname
            member.detail.profile?.address shouldBe request.profileAddress
            member.detail.introduction shouldBe request.introduction
        }

        "updateInfoFail" {
            shouldThrow<IllegalStateException> {
                member.updateInfo(MemberInfoUpdate("Leo", "toby100", "자기소개"))
            }
        }
    })
