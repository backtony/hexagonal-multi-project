package com.sample.hexagonal.splearn.domain.instructor

import com.sample.hexagonal.splearn.domain.AggregateRoot
import com.sample.hexagonal.splearn.domain.event.AbstractAggregateRoot
import com.sample.hexagonal.splearn.domain.member.Member
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne

@AggregateRoot
@Entity
class Instructor protected constructor(member: Member) : AbstractAggregateRoot() {
    @OneToOne(fetch = FetchType.LAZY)
    val member: Member = member

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    var status: InstructorStatus = InstructorStatus.PENDING
        protected set

    fun approve() {
        check(status == InstructorStatus.PENDING) { "강사의 상태가 PENDING 아닙니다" }

        status = InstructorStatus.ACTIVE
        markUpdated()
    }

    fun reject() {
        check(status == InstructorStatus.PENDING) { "강사의 상태가 PENDING 아닙니다" }

        status = InstructorStatus.REJECTED
        markUpdated()
    }

    fun isActive(): Boolean = status == InstructorStatus.ACTIVE

    fun ensureActive() {
        check(isActive()) { "ACTIVE 상태가 아닙니다" }
    }

    companion object {
        fun apply(member: Member): Instructor {
            check(member.isActive()) { "등록 완료 상태가 아닌 회원은 강사 신청을 할 수 없습니다" }

            return Instructor(member)
        }
    }
}
