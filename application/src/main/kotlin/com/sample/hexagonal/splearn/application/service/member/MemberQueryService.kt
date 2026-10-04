package com.sample.hexagonal.splearn.application.service.member

import com.sample.hexagonal.common.utils.exception.getOrThrowNotFound
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberFinder
import com.sample.hexagonal.splearn.application.service.member.port.outbound.MemberRepository
import com.sample.hexagonal.splearn.domain.member.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberQueryService(private val memberRepository: MemberRepository) : MemberFinder {
    override fun find(memberId: Long): Member = memberRepository.findById(memberId).getOrThrowNotFound(memberId)
}
