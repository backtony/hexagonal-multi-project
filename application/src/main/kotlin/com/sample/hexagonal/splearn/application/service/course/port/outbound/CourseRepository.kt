package com.sample.hexagonal.splearn.application.service.course.port.outbound

import com.sample.hexagonal.splearn.domain.course.Course
import com.sample.hexagonal.splearn.domain.instructor.Instructor

interface CourseRepository {
    fun save(course: Course): Course

    fun findById(courseId: Long): Course?

    fun findByTitleContaining(keyword: String): List<Course>

    fun findByInstructorId(instructorId: Long): List<Course>

    fun findByInstructorAndTitle(instructor: Instructor, title: String): Course?
}
