package com.sample.hexagonal.splearn.application

import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseRequestFixture
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRequestFixture
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseCreator
import com.sample.hexagonal.splearn.application.service.curriculum.port.inbound.CurriculumCoordinator
import com.sample.hexagonal.splearn.application.service.enrollment.port.inbound.Enroller
import com.sample.hexagonal.splearn.application.service.enrollment.port.inbound.EnrollRequest
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorApplication
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorApplyRequest
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRegister
import com.sample.hexagonal.splearn.domain.course.Course
import com.sample.hexagonal.splearn.domain.curriculum.Curriculum
import com.sample.hexagonal.splearn.domain.enrollment.Enrollment
import com.sample.hexagonal.splearn.domain.instructor.Instructor
import com.sample.hexagonal.splearn.domain.member.Member
import org.springframework.boot.test.context.TestComponent

@TestComponent
class ApplicationTestDataPreparer(
    private val memberRegister: MemberRegister,
    private val instructorApplication: InstructorApplication,
    private val courseCreator: CourseCreator,
    private val curriculumCoordinator: CurriculumCoordinator,
    private val enroller: Enroller,
) {
    fun prepareActiveMember(): Member = memberRegister.register(MemberRequestFixture.createMemberRegisterRequest()).apply { activate() }

    fun prepareInstructor(): Instructor = instructorApplication.apply(InstructorApplyRequest(prepareActiveMember().id!!)).apply {
        approve()
    }

    fun prepareCourse(): Course = courseCreator.create(CourseRequestFixture.createCourseCreateRequest(prepareInstructor().id!!)).apply {
        updateInfo(CourseRequestFixture.createCourseInfoUpdateRequest().toInfo())
    }

    fun preparePublishedCourse(): Course = prepareCourse().apply {
        submitForReview()
        publish()
    }

    fun prepareCurriculumSectionsAndLessons(course: Course): Curriculum = curriculumCoordinator.createCurriculum(course.id!!).apply {
        addSection("S1")
        addLesson(0, "L1")
        addLesson(0, "L2")
        addSection("S2")
        addLesson(1, "L3")
        addLesson(1, "L4")
        addSection("S3")
        addLesson(2, "L5")
    }

    fun prepareCourseWithCurriculum(): Course = prepareCourse().also { prepareCurriculumSectionsAndLessons(it) }

    fun prepareEnrollment(): Enrollment = enroller.enroll(EnrollRequest(prepareActiveMember().id!!, preparePublishedCourse().id!!))
}
