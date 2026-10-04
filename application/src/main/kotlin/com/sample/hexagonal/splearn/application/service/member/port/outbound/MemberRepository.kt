package com.sample.hexagonal.splearn.application.service.member.port.outbound

import com.sample.hexagonal.splearn.domain.member.Member
import com.sample.hexagonal.splearn.domain.member.Profile
import com.sample.hexagonal.splearn.domain.member.Email

/**
 * 회원 정보를 저장하거나 조회한다
 */
interface MemberRepository {
    fun save(member: Member): Member

    fun findByEmail(email: Email): Member?

    fun findById(memberId: Long): Member?

    fun findByProfile(profile: Profile): Member?
}
