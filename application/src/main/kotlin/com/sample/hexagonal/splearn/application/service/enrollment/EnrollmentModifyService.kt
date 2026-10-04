package com.sample.hexagonal.splearn.application.service.enrollment

import com.sample.hexagonal.common.utils.exception.getOrThrowNotFound
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseFinder
import com.sample.hexagonal.splearn.application.service.enrollment.port.inbound.Enroller
import com.sample.hexagonal.splearn.application.service.enrollment.port.inbound.EnrollRequest
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberFinder
import com.sample.hexagonal.splearn.application.service.enrollment.port.outbound.EnrollmentRepository
import com.sample.hexagonal.splearn.domain.course.Course
import com.sample.hexagonal.splearn.domain.enrollment.Enrollment
import com.sample.hexagonal.splearn.domain.member.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

@Service
@Transactional
@Validated
class EnrollmentModifyService(
    private val enrollmentRepository: EnrollmentRepository,
    private val memberFinder: MemberFinder,
    private val courseFinder: CourseFinder,
) : Enroller {
    override fun enroll(enrollRequest: EnrollRequest): Enrollment {
        val member = memberFinder.find(enrollRequest.memberId)
        val course = courseFinder.find(enrollRequest.courseId)

        checkDuplication(member, course)

        return enrollmentRepository.save(Enrollment.enroll(member, course))
    }

    override fun startStudying(enrollmentId: Long): Enrollment {
        val enrollment = enrollmentRepository.findById(enrollmentId).getOrThrowNotFound(enrollmentId)

        enrollment.startStudying()

        return enrollmentRepository.save(enrollment)
    }

    override fun complete(enrollmentId: Long): Enrollment {
        val enrollment = enrollmentRepository.findById(enrollmentId).getOrThrowNotFound(enrollmentId)

        enrollment.complete()

        return enrollmentRepository.save(enrollment)
    }

    private fun checkDuplication(member: Member, course: Course) {
        if (enrollmentRepository.findByMemberIdAndCourseId(member.id!!, course.id!!) != null) {
            throw IllegalArgumentException("이미 수강중인 강의입니다")
        }
    }
}
