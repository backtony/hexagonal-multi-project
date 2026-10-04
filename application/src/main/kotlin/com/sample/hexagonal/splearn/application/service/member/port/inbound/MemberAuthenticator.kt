package com.sample.hexagonal.splearn.application.service.member.port.inbound

import com.sample.hexagonal.splearn.domain.member.Member
import jakarta.validation.Valid

/**
 * 회원 인증
 * - ACTIVE 상태인 회원만 로그인할 수 있다
 */
interface MemberAuthenticator {
    fun login(@Valid loginRequest: MemberLoginRequest): Member
}
