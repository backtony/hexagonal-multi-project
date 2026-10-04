package com.sample.hexagonal.splearn.application.service.course.port.inbound

import com.sample.hexagonal.splearn.domain.course.CourseUpdateInfo
import jakarta.validation.constraints.Size

data class CourseInfoUpdateRequest(
    @field:Size(min = 2, max = 100) val title: String,
    @field:Size(max = 500) val description: String?,
) {
    fun toInfo(): CourseUpdateInfo = CourseUpdateInfo(title, description)
}
