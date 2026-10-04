package com.sample.hexagonal.splearn.application.service.member

import com.sample.hexagonal.splearn.application.service.member.port.inbound.LoginFailedException
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberAuthenticator
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberLoginRequest
import com.sample.hexagonal.splearn.application.service.member.port.outbound.MemberRepository
import com.sample.hexagonal.splearn.domain.member.Member
import com.sample.hexagonal.splearn.domain.member.PasswordEncoder
import com.sample.hexagonal.splearn.domain.member.Email
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

@Service
@Transactional(readOnly = true)
@Validated
class MemberAuthenticationService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) : MemberAuthenticator {
    override fun login(loginRequest: MemberLoginRequest): Member {
        val member = memberRepository.findByEmail(Email(loginRequest.email)) ?: throw LoginFailedException()

        if (!member.isActive()) throw LoginFailedException()

        if (!member.verifyPassword(loginRequest.password, passwordEncoder)) throw LoginFailedException()

        return member
    }
}
