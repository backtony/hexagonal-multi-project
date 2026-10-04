package com.sample.hexagonal.splearn.application.service.course.port.inbound

import com.sample.hexagonal.splearn.domain.course.Course

/**
 * 강의 공개와 관련된 작업
 */
interface CoursePublisher {
    fun submitForReview(courseId: Long): Course

    fun publish(courseId: Long): Course

    fun archive(courseId: Long): Course
}
