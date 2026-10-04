package com.sample.hexagonal.splearn.adapter.outbound.repository.instructor

import com.sample.hexagonal.splearn.application.service.instructor.port.outbound.InstructorRepository
import com.sample.hexagonal.splearn.domain.instructor.Instructor
import org.springframework.data.repository.Repository

interface InstructorJpaRepository :
    Repository<Instructor, Long>,
    InstructorRepository
