package com.sample.hexagonal.splearn.domain.instructor

import com.sample.hexagonal.splearn.domain.member.MemberFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class InstructorTest :
    StringSpec({
        "apply" {
            val member = MemberFixture.createActiveMember()

            val instructor = Instructor.apply(member)

            instructor.member shouldBe member
            instructor.status shouldBe InstructorStatus.PENDING
        }

        "applyFailedMemberNotActive" {
            val member = MemberFixture.createMember()

            shouldThrow<IllegalStateException> { Instructor.apply(member) }
        }

        "approve" {
            val instructor = InstructorFixture.createInstructor()

            instructor.approve()

            instructor.status shouldBe InstructorStatus.ACTIVE
        }

        "approveFailed" {
            val instructor = InstructorFixture.createActiveInstructor()

            shouldThrow<IllegalStateException> { instructor.approve() }
        }

        "reject" {
            val instructor = InstructorFixture.createInstructor()

            instructor.reject()

            instructor.status shouldBe InstructorStatus.REJECTED
        }

        "rejectFailed" {
            val instructor = InstructorFixture.createInstructor()
            instructor.reject()

            shouldThrow<IllegalStateException> { instructor.reject() }
        }

        "isActive" {
            val instructor = Instructor.apply(MemberFixture.createActiveMember())

            instructor.isActive() shouldBe false

            instructor.approve()

            instructor.isActive() shouldBe true
        }

        "ensureActive" {
            val instructor = Instructor.apply(MemberFixture.createActiveMember())

            shouldThrow<IllegalStateException> { instructor.ensureActive() }

            instructor.approve()
            instructor.ensureActive()
        }
    })
