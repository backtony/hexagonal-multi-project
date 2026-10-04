package com.sample.hexagonal.splearn.application.service.enrollment.port.outbound

import com.sample.hexagonal.splearn.domain.enrollment.Enrollment

interface EnrollmentRepository {
    fun save(enrollment: Enrollment): Enrollment

    fun findById(enrollmentId: Long): Enrollment?

    fun findByMemberId(memberId: Long): List<Enrollment>

    fun findByMemberIdAndCourseId(memberId: Long, courseId: Long): Enrollment?
}
