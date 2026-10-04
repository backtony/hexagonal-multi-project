package com.sample.hexagonal.splearn.application.service.course

import com.sample.hexagonal.common.utils.exception.getOrThrowNotFound
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseFinder
import com.sample.hexagonal.splearn.application.service.course.port.outbound.CourseRepository
import com.sample.hexagonal.splearn.domain.course.Course
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CourseQueryService(private val courseRepository: CourseRepository) : CourseFinder {
    override fun find(courseId: Long): Course = courseRepository.findById(courseId).getOrThrowNotFound(courseId)

    override fun findByTitle(keyword: String): List<Course> = courseRepository.findByTitleContaining(keyword)

    override fun findByInstructor(instructorId: Long): List<Course> = courseRepository.findByInstructorId(instructorId)
}
