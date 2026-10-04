package com.sample.hexagonal.splearn.application.service.course.port.inbound

import jakarta.validation.constraints.Size

data class CourseCreateRequest(
    val instructorId: Long,
    @field:Size(min = 2, max = 100) val title: String,
    @field:Size(max = 500) val description: String?,
)
