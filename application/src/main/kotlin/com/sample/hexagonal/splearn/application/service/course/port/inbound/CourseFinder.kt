package com.sample.hexagonal.splearn.application.service.course.port.inbound

import com.sample.hexagonal.splearn.domain.course.Course

/**
 * 강의를 조회
 */
interface CourseFinder {
    fun find(courseId: Long): Course

    fun findByTitle(keyword: String): List<Course>

    fun findByInstructor(instructorId: Long): List<Course>
}
