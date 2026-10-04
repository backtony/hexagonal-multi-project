package com.sample.hexagonal.splearn.domain.enrollment

import com.sample.hexagonal.splearn.domain.course.Course
import com.sample.hexagonal.splearn.domain.course.CourseFixture
import com.sample.hexagonal.splearn.domain.member.Member
import com.sample.hexagonal.splearn.domain.member.MemberFixture

object EnrollmentFixture {
    fun createEnrollment(
        member: Member = MemberFixture.createActiveMember(),
        course: Course = CourseFixture.createPublishedCourse(),
    ): Enrollment = Enrollment.enroll(member, course)
}
