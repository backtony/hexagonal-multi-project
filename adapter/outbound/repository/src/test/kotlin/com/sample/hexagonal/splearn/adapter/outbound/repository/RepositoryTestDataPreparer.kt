package com.sample.hexagonal.splearn.adapter.outbound.repository

import com.sample.hexagonal.splearn.application.service.course.port.outbound.CourseRepository
import com.sample.hexagonal.splearn.application.service.enrollment.port.outbound.EnrollmentRepository
import com.sample.hexagonal.splearn.application.service.instructor.port.outbound.InstructorRepository
import com.sample.hexagonal.splearn.application.service.member.port.outbound.MemberRepository
import com.sample.hexagonal.splearn.domain.course.Course
import com.sample.hexagonal.splearn.domain.course.CourseFixture
import com.sample.hexagonal.splearn.domain.course.CourseUpdateInfo
import com.sample.hexagonal.splearn.domain.enrollment.Enrollment
import com.sample.hexagonal.splearn.domain.enrollment.EnrollmentFixture
import com.sample.hexagonal.splearn.domain.instructor.Instructor
import com.sample.hexagonal.splearn.domain.instructor.InstructorFixture
import com.sample.hexagonal.splearn.domain.member.Member
import com.sample.hexagonal.splearn.domain.member.MemberFixture
import jakarta.persistence.EntityManager
import org.hibernate.SessionFactory
import org.hibernate.stat.Statistics
import org.springframework.boot.test.context.TestComponent

@TestComponent
class RepositoryTestDataPreparer(
    private val entityManager: EntityManager,
    private val memberRepository: MemberRepository,
    private val instructorRepository: InstructorRepository,
    private val courseRepository: CourseRepository,
    private val enrollmentRepository: EnrollmentRepository,
) {
    fun prepareActiveMember(): Member = memberRepository.save(MemberFixture.createActiveMember())

    fun prepareActiveInstructor(member: Member = prepareActiveMember()): Instructor =
        instructorRepository.save(InstructorFixture.createActiveInstructor(member))

    fun prepareCourse(instructor: Instructor = prepareActiveInstructor(), title: String? = null): Course =
        courseRepository.save(CourseFixture.createCourse(instructor, title))

    fun preparePublishedCourse(): Course = prepareCourse().apply {
        updateInfo(CourseUpdateInfo(title, "강의 소개"))
        submitForReview()
        publish()
    }

    fun prepareEnrollment(member: Member, course: Course): Enrollment =
        enrollmentRepository.save(EnrollmentFixture.createEnrollment(member, course))

    fun prepareStatistics(): Statistics = entityManager.entityManagerFactory.unwrap(SessionFactory::class.java).statistics.apply {
        isStatisticsEnabled = true
        clear()
    }
}
