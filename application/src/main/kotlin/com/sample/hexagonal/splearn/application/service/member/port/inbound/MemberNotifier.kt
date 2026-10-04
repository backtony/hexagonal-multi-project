package com.sample.hexagonal.splearn.application.service.member.port.inbound

/**
 * 회원에게 알림을 보낸다
 */
interface MemberNotifier {
    fun sendWelcomeEmail(memberId: Long)
}
