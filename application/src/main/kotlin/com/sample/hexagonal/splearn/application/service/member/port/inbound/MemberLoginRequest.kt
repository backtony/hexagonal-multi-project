package com.sample.hexagonal.splearn.application.service.member.port.inbound

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class MemberLoginRequest(
    @field:Email val email: String,
    @field:Size(min = 8, max = 100) val password: String,
)
