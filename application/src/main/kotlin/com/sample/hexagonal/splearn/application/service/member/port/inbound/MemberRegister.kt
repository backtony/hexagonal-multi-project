package com.sample.hexagonal.splearn.application.service.member.port.inbound

import com.sample.hexagonal.splearn.domain.member.Member
import jakarta.validation.Valid

/**
 * 회원의 등록과 관련된 기능을 제공한다
 */
interface MemberRegister {
    fun register(@Valid registerRequest: MemberRegisterRequest): Member

    fun activate(memberId: Long): Member

    fun deactivate(memberId: Long): Member

    fun updateInfo(memberId: Long, @Valid memberInfoUpdateRequest: MemberInfoUpdateRequest): Member
}
