package com.sample.hexagonal.splearn.application.service.member.port.inbound

import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberRegisterRequest
import org.instancio.Instancio
import org.instancio.Select

object MemberRequestFixture {
    fun createMemberRegisterRequest(email: String = Instancio.gen().net().email().get()): MemberRegisterRequest =
        Instancio.of(MemberRegisterRequest::class.java)
            .set(Select.field(MemberRegisterRequest::class.java, "email"), email)
            .create()
}
