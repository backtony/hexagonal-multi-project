package com.sample.hexagonal.splearn.adapter.inbound.controller.member.dto

import com.sample.hexagonal.splearn.domain.member.Member

data class MemberRegisterResponse(
    val memberId: Long?,
    val email: String,
) {
    companion object {
        fun of(member: Member): MemberRegisterResponse = MemberRegisterResponse(member.id, member.email.address)
    }
}
