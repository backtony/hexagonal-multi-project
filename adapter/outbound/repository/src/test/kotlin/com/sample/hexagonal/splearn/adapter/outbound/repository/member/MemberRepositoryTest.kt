package com.sample.hexagonal.splearn.adapter.outbound.repository.member

import com.sample.hexagonal.splearn.application.service.member.port.outbound.MemberRepository
import com.sample.hexagonal.splearn.domain.member.Member
import com.sample.hexagonal.splearn.domain.member.MemberFixture.createMemberRegisterInfo
import com.sample.hexagonal.splearn.domain.member.MemberFixture.createPasswordEncoder
import com.sample.hexagonal.splearn.domain.member.MemberStatus
import com.sample.hexagonal.splearn.adapter.outbound.repository.RepositoryTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import org.springframework.dao.DataIntegrityViolationException

@RepositoryTest
class MemberRepositoryTest(
    memberRepository: MemberRepository,
    entityManager: EntityManager,
) : StringSpec({
    "createMember" {
        val member = Member.register(createMemberRegisterInfo(), createPasswordEncoder())
        member.id.shouldBeNull()

        memberRepository.save(member)

        member.id.shouldNotBeNull()

        entityManager.flush()
        entityManager.clear()

        val found = memberRepository.findById(member.id!!).shouldNotBeNull()
        found.status shouldBe MemberStatus.PENDING
        found.detail.createdAt.shouldNotBeNull()
    }

    "auditTimestamps" {
        val member = memberRepository.save(Member.register(createMemberRegisterInfo(), createPasswordEncoder()))
        entityManager.flush()
        val createdAt = member.createdAt

        member.activate()
        entityManager.flush()
        entityManager.clear()

        val found = memberRepository.findById(member.id!!).shouldNotBeNull()
        found.createdAt shouldBe createdAt
        found.updatedAt shouldBe member.updatedAt
    }

    "duplicateEmailFail" {
        val registerInfo = createMemberRegisterInfo()
        memberRepository.save(Member.register(registerInfo, createPasswordEncoder()))

        val member2 = Member.register(registerInfo, createPasswordEncoder())

        shouldThrow<DataIntegrityViolationException> { memberRepository.save(member2) }
    }
})
