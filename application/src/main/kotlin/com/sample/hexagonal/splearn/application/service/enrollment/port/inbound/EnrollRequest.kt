package com.sample.hexagonal.splearn.application.service.enrollment.port.inbound

data class EnrollRequest(
    val memberId: Long,
    val courseId: Long,
)
