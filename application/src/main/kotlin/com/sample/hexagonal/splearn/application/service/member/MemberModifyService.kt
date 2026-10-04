package com.sample.hexagonal.splearn.application.service.member

import com.sample.hexagonal.common.utils.exception.getOrThrowNotFound
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRegister
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRegisterRequest
import com.sample.hexagonal.splearn.application.service.member.port.outbound.EmailSender
import com.sample.hexagonal.splearn.application.service.member.port.outbound.MemberRepository
import com.sample.hexagonal.splearn.application.support.event.DomainEventPublisher
import com.sample.hexagonal.splearn.domain.member.DuplicateEmailException
import com.sample.hexagonal.splearn.domain.member.DuplicateProfileException
import com.sample.hexagonal.splearn.domain.member.Member
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberInfoUpdateRequest
import com.sample.hexagonal.splearn.domain.member.PasswordEncoder
import com.sample.hexagonal.splearn.domain.member.Profile
import com.sample.hexagonal.splearn.domain.member.Email
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

@Service
@Transactional
@Validated
class MemberModifyService(
    private val memberRepository: MemberRepository,
    private val emailSender: EmailSender,
    private val passwordEncoder: PasswordEncoder,
    private val domainEventPublisher: DomainEventPublisher,
) : MemberRegister {
    override fun register(registerRequest: MemberRegisterRequest): Member {
        checkDuplicateEmail(registerRequest)

        val member = Member.register(registerRequest.toInfo(), passwordEncoder)

        memberRepository.save(member)
        member.pullDomainEvents().forEach(domainEventPublisher::publishEvent)

        sendWelcomeEmail(member)

        return member
    }

    override fun activate(memberId: Long): Member {
        val member = memberRepository.findById(memberId).getOrThrowNotFound(memberId)

        member.activate()

        return memberRepository.save(member)
    }

    override fun deactivate(memberId: Long): Member {
        val member = memberRepository.findById(memberId).getOrThrowNotFound(memberId)

        member.deactivate()

        return memberRepository.save(member)
    }

    override fun updateInfo(memberId: Long, memberInfoUpdateRequest: MemberInfoUpdateRequest): Member {
        val member = memberRepository.findById(memberId).getOrThrowNotFound(memberId)

        checkDuplicateProfile(member, memberInfoUpdateRequest.profileAddress)

        member.updateInfo(memberInfoUpdateRequest.toInfo())

        return memberRepository.save(member)
    }

    private fun checkDuplicateProfile(member: Member, profileAddress: String) {
        if (profileAddress.isEmpty()) return

        if (member.detail.profile?.address == profileAddress) return

        if (memberRepository.findByProfile(Profile(profileAddress)) != null) {
            throw DuplicateProfileException("이미 존재하는 프로필 주소입니다: $profileAddress")
        }
    }

    private fun sendWelcomeEmail(member: Member) {
        emailSender.send(member.email, "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요")
    }

    private fun checkDuplicateEmail(registerRequest: MemberRegisterRequest) {
        if (memberRepository.findByEmail(Email(registerRequest.email)) != null) {
            throw DuplicateEmailException("이미 사용중인 이메일입니다: ${registerRequest.email}")
        }
    }
}
