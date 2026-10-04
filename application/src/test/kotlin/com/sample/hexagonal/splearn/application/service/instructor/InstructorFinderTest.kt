package com.sample.hexagonal.splearn.application.service.instructor

import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorApplication
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorApplyRequest
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorFinder
import com.sample.hexagonal.splearn.application.ApplicationServiceTest
import com.sample.hexagonal.splearn.application.ApplicationTestDataPreparer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

@ApplicationServiceTest
class InstructorFinderTest(
    instructorFinder: InstructorFinder,
    instructorApplication: InstructorApplication,
    preparer: ApplicationTestDataPreparer,
) : StringSpec({
    "findByMember" {
        val member = preparer.prepareActiveMember()
        val instructor = instructorApplication.apply(InstructorApplyRequest(member.id!!))

        instructorFinder.findByMember(member.id!!) shouldBe instructor
        instructorFinder.findByMember(Long.MAX_VALUE).shouldBeNull()
    }
})
