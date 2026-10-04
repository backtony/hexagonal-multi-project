package com.sample.hexagonal.splearn.application.service.course.port.inbound

import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseCreateRequest
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseInfoUpdateRequest
import com.sample.hexagonal.splearn.domain.course.CourseFixture
import org.instancio.Instancio

object CourseRequestFixture {
    fun createCourseCreateRequest(instructorId: Long, title: String? = null): CourseCreateRequest = CourseCreateRequest(
        instructorId,
        title ?: CourseFixture.createTitle(),
        Instancio.gen().string().maxLength(500).nullable().get(),
    )

    fun createCourseInfoUpdateRequest(title: String? = null): CourseInfoUpdateRequest = CourseInfoUpdateRequest(
        title ?: CourseFixture.createTitle(),
        Instancio.gen().string().maxLength(500).get(),
    )
}
