package com.sample.hexagonal.splearn.application.service.member.port.inbound

import com.sample.hexagonal.splearn.domain.member.MemberInfoUpdate
import jakarta.validation.constraints.Size

data class MemberInfoUpdateRequest(
    @field:Size(min = 5, max = 20) val nickname: String,
    @field:Size(max = 15) val profileAddress: String,
    val introduction: String,
) {
    fun toInfo(): MemberInfoUpdate = MemberInfoUpdate(nickname, profileAddress, introduction)
}
