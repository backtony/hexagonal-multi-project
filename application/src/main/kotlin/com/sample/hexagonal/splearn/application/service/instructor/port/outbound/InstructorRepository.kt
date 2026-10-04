package com.sample.hexagonal.splearn.application.service.instructor.port.outbound

import com.sample.hexagonal.splearn.domain.instructor.Instructor

interface InstructorRepository {
    fun save(instructor: Instructor): Instructor

    fun findById(instructorId: Long): Instructor?

    fun findByMemberId(memberId: Long): Instructor?
}
