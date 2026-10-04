package com.sample.hexagonal.splearn.server.api

import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseCreator
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseRequestFixture
import com.sample.hexagonal.splearn.application.service.curriculum.port.inbound.CurriculumFinder
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorApplication
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorApplyRequest
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRegister
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRequestFixture
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class CourseCreationTest(
    memberRegister: MemberRegister,
    instructorApplication: InstructorApplication,
    courseCreator: CourseCreator,
    curriculumFinder: CurriculumFinder,
) : StringSpec({
    "강의를 생성하면 같은 트랜잭션에서 커리큘럼이 생성된다" {
        val member = memberRegister.register(MemberRequestFixture.createMemberRegisterRequest())
        memberRegister.activate(member.id!!)
        val instructor = instructorApplication.apply(InstructorApplyRequest(member.id!!))
        instructorApplication.approve(instructor.id!!)

        val course = courseCreator.create(CourseRequestFixture.createCourseCreateRequest(instructor.id!!))

        curriculumFinder.findByCourse(course.id!!).course.id shouldBe course.id
    }
})
