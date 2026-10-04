package com.sample.hexagonal.splearn.adapter.outbound.repository.course

import com.sample.hexagonal.splearn.application.service.course.port.outbound.CourseRepository
import com.sample.hexagonal.splearn.domain.course.Course
import org.springframework.data.repository.Repository

interface CourseJpaRepository :
    Repository<Course, Long>,
    CourseRepository
