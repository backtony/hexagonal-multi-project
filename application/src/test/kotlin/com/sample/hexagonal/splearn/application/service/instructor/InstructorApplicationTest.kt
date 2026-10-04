package com.sample.hexagonal.splearn.application.service.instructor

import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.DuplicateInstructorApplicationException
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorApplication
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorApplyRequest
import com.sample.hexagonal.splearn.application.service.instructor.port.outbound.InstructorRepository
import com.sample.hexagonal.splearn.domain.instructor.Instructor
import com.sample.hexagonal.splearn.domain.instructor.InstructorStatus
import com.sample.hexagonal.splearn.application.ApplicationServiceTest
import com.sample.hexagonal.splearn.application.ApplicationTestDataPreparer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@ApplicationServiceTest
class InstructorApplicationTest(
    instructorApplication: InstructorApplication,
    instructorRepository: InstructorRepository,
    preparer: ApplicationTestDataPreparer,
) : StringSpec({
    fun preparePendingInstructor(): Instructor = instructorApplication.apply(InstructorApplyRequest(preparer.prepareActiveMember().id!!))

    "apply" {
        val member = preparer.prepareActiveMember()

        val instructor = instructorApplication.apply(InstructorApplyRequest(member.id!!))

        instructor.id.shouldNotBeNull()
        instructor.status shouldBe InstructorStatus.PENDING
        instructorRepository.findById(instructor.id!!).shouldNotBeNull()
    }

    "duplicateApply" {
        val member = preparer.prepareActiveMember()
        instructorApplication.apply(InstructorApplyRequest(member.id!!))

        shouldThrow<DuplicateInstructorApplicationException> {
            instructorApplication.apply(InstructorApplyRequest(member.id!!))
        }
    }

    "approve" {
        val instructor = instructorApplication.approve(preparePendingInstructor().id!!)

        instructor.status shouldBe InstructorStatus.ACTIVE
    }

    "reject" {
        val instructor = instructorApplication.reject(preparePendingInstructor().id!!)

        instructor.status shouldBe InstructorStatus.REJECTED
    }
})
