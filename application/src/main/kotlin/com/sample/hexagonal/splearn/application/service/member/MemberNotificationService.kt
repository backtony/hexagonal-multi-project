package com.sample.hexagonal.splearn.application.service.member

import com.sample.hexagonal.common.utils.exception.getOrThrowNotFound
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberNotifier
import com.sample.hexagonal.splearn.application.service.member.port.outbound.EmailSender
import com.sample.hexagonal.splearn.application.service.member.port.outbound.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberNotificationService(
    private val memberRepository: MemberRepository,
    private val emailSender: EmailSender,
) : MemberNotifier {
    override fun sendWelcomeEmail(memberId: Long) {
        val member = memberRepository.findById(memberId).getOrThrowNotFound(memberId)
        emailSender.send(member.email, "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요")
    }
}
