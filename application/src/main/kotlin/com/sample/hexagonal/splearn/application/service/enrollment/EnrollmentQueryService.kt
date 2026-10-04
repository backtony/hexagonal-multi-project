package com.sample.hexagonal.splearn.application.service.enrollment

import com.sample.hexagonal.common.utils.exception.getOrThrowNotFound
import com.sample.hexagonal.splearn.application.service.enrollment.port.inbound.EnrollmentFinder
import com.sample.hexagonal.splearn.application.service.enrollment.port.outbound.EnrollmentRepository
import com.sample.hexagonal.splearn.domain.enrollment.Enrollment
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class EnrollmentQueryService(private val enrollmentRepository: EnrollmentRepository) : EnrollmentFinder {
    override fun find(enrollmentId: Long): Enrollment = enrollmentRepository.findById(enrollmentId).getOrThrowNotFound(enrollmentId)

    override fun findByMember(memberId: Long): List<Enrollment> = enrollmentRepository.findByMemberId(memberId)

    override fun findByMemberAndCourse(memberId: Long, courseId: Long): Enrollment? =
        enrollmentRepository.findByMemberIdAndCourseId(memberId, courseId)
}
