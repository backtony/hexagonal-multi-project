package com.sample.hexagonal.splearn.application.service.instructor

import com.sample.hexagonal.common.utils.exception.getOrThrowNotFound
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorFinder
import com.sample.hexagonal.splearn.application.service.instructor.port.outbound.InstructorRepository
import com.sample.hexagonal.splearn.domain.instructor.Instructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class InstructorQueryService(private val instructorRepository: InstructorRepository) : InstructorFinder {
    override fun find(instructorId: Long): Instructor = instructorRepository.findById(instructorId).getOrThrowNotFound(instructorId)

    override fun findByMember(memberId: Long): Instructor? = instructorRepository.findByMemberId(memberId)
}
