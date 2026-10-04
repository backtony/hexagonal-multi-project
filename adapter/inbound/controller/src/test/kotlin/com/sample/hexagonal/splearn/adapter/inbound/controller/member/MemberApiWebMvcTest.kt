package com.sample.hexagonal.splearn.adapter.inbound.controller.member

import tools.jackson.databind.json.JsonMapper
import com.ninjasquad.springmockk.MockkBean
import com.sample.hexagonal.splearn.adapter.inbound.controller.member.MemberController
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRegister
import com.sample.hexagonal.splearn.domain.member.MemberFixture
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRequestFixture
import com.sample.hexagonal.splearn.domain.member.DuplicateEmailException
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.verify
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(MemberController::class)
class MemberApiWebMvcTest(
    mockMvc: MockMvc,
    objectMapper: JsonMapper,
    @MockkBean val memberRegister: MemberRegister,
) : StringSpec({
    "register" {
        every { memberRegister.register(any()) } returns MemberFixture.createMember(1L)
        val request = MemberRequestFixture.createMemberRegisterRequest()

        mockMvc.post("/api/members") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.memberId") { value(1) }
        }

        verify { memberRegister.register(request) }
    }

    "registerFailDuplicateEmail" {
        every { memberRegister.register(any()) } throws DuplicateEmailException("이미 사용중인 이메일입니다")
        val request = MemberRequestFixture.createMemberRegisterRequest()

        mockMvc.post("/api/members") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isConflict() }
        }
    }

    "registerFail" {
        val request = MemberRequestFixture.createMemberRegisterRequest("invalid email")

        mockMvc.post("/api/members") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
        }
    }
})
