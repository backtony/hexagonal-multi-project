package com.sample.hexagonal.splearn.application.service.member

import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRegister
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRegisterRequest
import com.sample.hexagonal.splearn.domain.member.DuplicateEmailException
import com.sample.hexagonal.splearn.domain.member.DuplicateProfileException
import com.sample.hexagonal.splearn.domain.member.Member
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberInfoUpdateRequest
import com.sample.hexagonal.splearn.domain.member.MemberStatus
import com.sample.hexagonal.splearn.domain.member.event.MemberRegisteredEvent
import com.sample.hexagonal.splearn.application.RecordingDomainEventPublisher
import com.sample.hexagonal.splearn.application.ApplicationServiceTest
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRequestFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldContainExactly
import jakarta.persistence.EntityManager
import jakarta.validation.ConstraintViolationException

@ApplicationServiceTest
class MemberRegisterTest(
    memberRegister: MemberRegister,
    entityManager: EntityManager,
    domainEventPublisher: RecordingDomainEventPublisher,
) : StringSpec({
    fun registerMember(email: String? = null): Member {
        val request =
            email?.let(MemberRequestFixture::createMemberRegisterRequest) ?: MemberRequestFixture.createMemberRegisterRequest()
        val member = memberRegister.register(request)
        entityManager.flush()
        entityManager.clear()
        return member
    }

    fun checkValidation(invalid: MemberRegisterRequest) {
        shouldThrow<ConstraintViolationException> { memberRegister.register(invalid) }
    }

    "register" {
        val member = memberRegister.register(MemberRequestFixture.createMemberRegisterRequest())

        member.id.shouldNotBeNull()
        member.status shouldBe MemberStatus.PENDING
    }

    "registerPublishesMemberRegisteredEvent" {
        domainEventPublisher.events.clear()
        val member = memberRegister.register(MemberRequestFixture.createMemberRegisterRequest())

        domainEventPublisher.events.filterIsInstance<MemberRegisteredEvent>()
            .map { it.memberId } shouldContainExactly listOf(member.id)
    }

    "duplicateEmailFail" {
        val memberRegisterRequest = MemberRequestFixture.createMemberRegisterRequest()

        memberRegister.register(memberRegisterRequest)

        shouldThrow<DuplicateEmailException> { memberRegister.register(memberRegisterRequest) }
    }

    "activate" {
        val registered = registerMember()

        val member = memberRegister.activate(registered.id!!)
        entityManager.flush()

        member.status shouldBe MemberStatus.ACTIVE
        member.detail.activatedAt.shouldNotBeNull()
    }

    "deactivate" {
        val registered = registerMember()

        memberRegister.activate(registered.id!!)
        entityManager.flush()
        entityManager.clear()

        val member = memberRegister.deactivate(registered.id!!)

        member.status shouldBe MemberStatus.DEACTIVATED
        member.detail.deactivatedAt.shouldNotBeNull()
    }

    "updateInfo" {
        val registered = registerMember()

        memberRegister.activate(registered.id!!)
        entityManager.flush()
        entityManager.clear()

        val member = memberRegister.updateInfo(registered.id!!, MemberInfoUpdateRequest("Peter", "toby100", "자기소개"))

        member.detail.profile?.address shouldBe "toby100"
    }

    "updateInfoFail" {
        val member = registerMember()
        memberRegister.activate(member.id!!)
        memberRegister.updateInfo(member.id!!, MemberInfoUpdateRequest("Peter", "toby100", "자기소개"))

        val member2 = registerMember("toby2@splearn.app")
        memberRegister.activate(member2.id!!)
        entityManager.flush()
        entityManager.clear()

        // member2는 기존의 member와 같은 프로필 주소를 사용할 수 없다
        shouldThrow<DuplicateProfileException> {
            memberRegister.updateInfo(member2.id!!, MemberInfoUpdateRequest("James", "toby100", "Introduction"))
        }

        // 다른 프로필 주소로는 변경 가능
        memberRegister.updateInfo(member2.id!!, MemberInfoUpdateRequest("James", "toby101", "Introduction"))

        // 기존 프로필 주소를 바꾸는 것도 가능
        memberRegister.updateInfo(member.id!!, MemberInfoUpdateRequest("James", "toby100", "Introduction"))

        // 프로필 주소 중복는 허용하지 않음
        shouldThrow<DuplicateProfileException> {
            memberRegister.updateInfo(member.id!!, MemberInfoUpdateRequest("James", "toby101", "Introduction"))
        }

        // 프로필 주소를 제거하는 것도 가능
        memberRegister.updateInfo(member.id!!, MemberInfoUpdateRequest("James", "", "Introduction"))
        memberRegister.updateInfo(member2.id!!, MemberInfoUpdateRequest("James", "", "Introduction"))

        entityManager.flush()
    }

    "memberRegisterRequestFail" {
        checkValidation(MemberRegisterRequest("toby@splearn.app", "Toby", "longsecret"))
        checkValidation(MemberRegisterRequest("toby@splearn.app", "Charlie_____________________________", "longsecret"))
        checkValidation(MemberRegisterRequest("tobysplearn.app", "Charlie", "longsecret"))
    }
})
