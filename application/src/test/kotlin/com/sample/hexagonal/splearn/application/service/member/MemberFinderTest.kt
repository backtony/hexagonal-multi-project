package com.sample.hexagonal.splearn.application.service.member

import com.sample.hexagonal.common.utils.exception.ResourceNotFoundException
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberFinder
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRegister
import com.sample.hexagonal.splearn.application.ApplicationServiceTest
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRequestFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager

@ApplicationServiceTest
class MemberFinderTest(
    memberFinder: MemberFinder,
    memberRegister: MemberRegister,
    entityManager: EntityManager,
) : StringSpec({
    "find" {
        val member = memberRegister.register(MemberRequestFixture.createMemberRegisterRequest())
        entityManager.flush()
        entityManager.clear()

        val found = memberFinder.find(member.id!!)

        member.id shouldBe found.id
    }

    "findFail" {
        shouldThrow<ResourceNotFoundException> { memberFinder.find(999L) }
    }
})
