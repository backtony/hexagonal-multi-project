package com.sample.hexagonal.splearn.application.service.enrollment

import com.sample.hexagonal.splearn.application.service.enrollment.port.inbound.Enroller
import com.sample.hexagonal.splearn.application.service.enrollment.port.inbound.EnrollRequest
import com.sample.hexagonal.splearn.domain.enrollment.EnrollmentStatus
import com.sample.hexagonal.splearn.application.ApplicationServiceTest
import com.sample.hexagonal.splearn.application.ApplicationTestDataPreparer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@ApplicationServiceTest
class EnrollerTest(
    enroller: Enroller,
    preparer: ApplicationTestDataPreparer,
) : StringSpec({
    "enroll" {
        val member = preparer.prepareActiveMember()
        val course = preparer.preparePublishedCourse()

        enroller.enroll(EnrollRequest(member.id!!, course.id!!)).id.shouldNotBeNull()
    }

    "enrollFailDuplicate" {
        val enrollment = preparer.prepareEnrollment()

        shouldThrow<IllegalArgumentException> {
            enroller.enroll(EnrollRequest(enrollment.member.id!!, enrollment.course.id!!))
        }
    }

    "startStudying" {
        val enrollment = preparer.prepareEnrollment()

        enroller.startStudying(enrollment.id!!).status shouldBe EnrollmentStatus.STUDYING
    }

    "complete" {
        val enrollment = preparer.prepareEnrollment()
        enroller.startStudying(enrollment.id!!)

        enroller.complete(enrollment.id!!).status shouldBe EnrollmentStatus.COMPLETED
    }
})
