package com.sample.hexagonal.splearn.application.service.enrollment.port.inbound

import com.sample.hexagonal.splearn.domain.enrollment.Enrollment
import jakarta.validation.Valid

/**
 * 수강 신청과 관리를 담당
 */
interface Enroller {
    fun enroll(@Valid enrollRequest: EnrollRequest): Enrollment

    fun startStudying(enrollmentId: Long): Enrollment

    fun complete(enrollmentId: Long): Enrollment
}
