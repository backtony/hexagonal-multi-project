package com.sample.hexagonal.splearn.application.service.instructor.port.inbound

import com.sample.hexagonal.splearn.domain.instructor.Instructor

/**
 * 강사 조회
 */
interface InstructorFinder {
    fun find(instructorId: Long): Instructor

    fun findByMember(memberId: Long): Instructor?
}
