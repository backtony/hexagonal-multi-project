package com.sample.hexagonal.splearn.domain.instructor

import com.sample.hexagonal.splearn.domain.member.Member
import com.sample.hexagonal.splearn.domain.member.MemberFixture

object InstructorFixture {
    fun createInstructor(member: Member = MemberFixture.createActiveMember()): Instructor = Instructor.apply(member)

    fun createActiveInstructor(member: Member = MemberFixture.createActiveMember()): Instructor = createInstructor(member).apply {
        approve()
    }
}
