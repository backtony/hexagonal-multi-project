package com.sample.hexagonal.splearn.application.service.instructor.port.inbound

import com.sample.hexagonal.splearn.domain.instructor.Instructor
import jakarta.validation.Valid

/**
 * 강사 신청
 */
interface InstructorApplication {
    fun apply(@Valid applyRequest: InstructorApplyRequest): Instructor

    fun approve(instructorId: Long): Instructor

    fun reject(instructorId: Long): Instructor
}
