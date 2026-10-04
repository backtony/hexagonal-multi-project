package com.sample.hexagonal.splearn.adapter.outbound.repository.member

import com.sample.hexagonal.splearn.application.service.member.port.outbound.MemberRepository
import com.sample.hexagonal.splearn.domain.member.Member
import com.sample.hexagonal.splearn.domain.member.Profile
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

interface MemberJpaRepository :
    Repository<Member, Long>,
    MemberRepository {
    @Query("select m from Member m where m.detail.profile = :profile")
    override fun findByProfile(profile: Profile): Member?
}
