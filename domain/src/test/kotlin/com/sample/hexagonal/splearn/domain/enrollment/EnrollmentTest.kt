package com.sample.hexagonal.splearn.domain.enrollment

import com.sample.hexagonal.splearn.domain.course.CourseFixture
import com.sample.hexagonal.splearn.domain.member.MemberFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class EnrollmentTest :
    StringSpec({
        "enroll" {
            val enrollment = Enrollment.enroll(MemberFixture.createActiveMember(), CourseFixture.createPublishedCourse())

            enrollment.status shouldBe EnrollmentStatus.ENROLLED
            enrollment.createdAt.shouldNotBeNull()
        }

        "enrollFailNotPublishedCourse" {
            val member = MemberFixture.createActiveMember()
            val course = CourseFixture.createCourse()

            shouldThrow<IllegalStateException> { Enrollment.enroll(member, course) }
        }

        "startStudying" {
            val enrollment = EnrollmentFixture.createEnrollment()

            enrollment.startStudying()

            enrollment.status shouldBe EnrollmentStatus.STUDYING
            shouldThrow<IllegalStateException> { enrollment.startStudying() }
        }

        "complete" {
            val enrollment = EnrollmentFixture.createEnrollment()
            enrollment.startStudying()

            enrollment.complete()

            enrollment.status shouldBe EnrollmentStatus.COMPLETED
            enrollment.completedAt.shouldNotBeNull()
            shouldThrow<IllegalStateException> { enrollment.complete() }
        }
    })
