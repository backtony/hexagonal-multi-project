package com.sample.hexagonal.splearn.adapter.outbound.repository.enrollment

import com.sample.hexagonal.splearn.application.service.enrollment.port.outbound.EnrollmentRepository
import com.sample.hexagonal.splearn.domain.enrollment.Enrollment
import org.springframework.data.repository.Repository

interface EnrollmentJpaRepository :
    Repository<Enrollment, Long>,
    EnrollmentRepository
