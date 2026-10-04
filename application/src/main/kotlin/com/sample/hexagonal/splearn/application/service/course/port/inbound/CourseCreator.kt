package com.sample.hexagonal.splearn.application.service.course.port.inbound

import com.sample.hexagonal.splearn.domain.course.Course
import jakarta.validation.Valid

/**
 * 강의를 준비하는 작업
 */
interface CourseCreator {
    fun create(@Valid createRequest: CourseCreateRequest): Course

    fun updateInfo(courseId: Long, @Valid infoUpdateRequest: CourseInfoUpdateRequest): Course
}
