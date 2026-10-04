package com.sample.hexagonal.splearn.application.service.enrollment.port.inbound

import com.sample.hexagonal.splearn.domain.enrollment.Enrollment

interface EnrollmentFinder {
    fun find(enrollmentId: Long): Enrollment

    fun findByMember(memberId: Long): List<Enrollment>

    fun findByMemberAndCourse(memberId: Long, courseId: Long): Enrollment?
}
