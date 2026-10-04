package com.sample.hexagonal.splearn.adapter.inbound.controller.member

import com.sample.hexagonal.splearn.adapter.inbound.controller.member.dto.MemberRegisterResponse
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRegister
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRegisterRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(private val memberRegister: MemberRegister) {
    @PostMapping("/api/members")
    fun register(@RequestBody @Valid request: MemberRegisterRequest): MemberRegisterResponse =
        MemberRegisterResponse.of(memberRegister.register(request))
}
