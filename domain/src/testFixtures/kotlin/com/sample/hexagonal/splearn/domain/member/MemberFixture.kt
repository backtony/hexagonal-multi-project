package com.sample.hexagonal.splearn.domain.member

import com.sample.hexagonal.splearn.domain.EntityFixture
import org.instancio.Instancio
import org.instancio.Select

object MemberFixture {
    fun createMemberRegisterInfo(email: String = Instancio.gen().net().email().get()): MemberRegisterInfo =
        Instancio.of(MemberRegisterInfo::class.java)
            .set(Select.field(MemberRegisterInfo::class.java, "email"), email)
            .create()

    fun createPasswordEncoder(): PasswordEncoder = object : PasswordEncoder {
        override fun encode(password: String): String = password.uppercase()

        override fun matches(password: String, passwordHash: String): Boolean = encode(password) == passwordHash
    }

    fun createMember(): Member = Member.register(createMemberRegisterInfo(), createPasswordEncoder())

    fun createMember(id: Long): Member = createMember().also { EntityFixture.assignId(it, id) }

    fun createMember(email: String): Member = Member.register(createMemberRegisterInfo(email), createPasswordEncoder())

    fun createActiveMember(): Member = createMember().apply { activate() }
}
